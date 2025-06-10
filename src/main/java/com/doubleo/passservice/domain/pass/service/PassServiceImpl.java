package com.doubleo.passservice.domain.pass.service;

import com.doubleo.hospitalservice.domain.area.grpc.server.AreaResponse;
import com.doubleo.memberservice.domain.member.grpc.server.MemberResponse;
import com.doubleo.passservice.domain.notification.dto.request.FcmSendRequest;
import com.doubleo.passservice.domain.notification.service.FcmService;
import com.doubleo.passservice.domain.pass.domain.Pass;
import com.doubleo.passservice.domain.pass.domain.PassArea;
import com.doubleo.passservice.domain.pass.dto.GuardianInfo;
import com.doubleo.passservice.domain.pass.dto.response.MemberPassInfoResponse;
import com.doubleo.passservice.domain.pass.dto.response.PassCreateResponse;
import com.doubleo.passservice.domain.pass.dto.response.PendingPassResponse;
import com.doubleo.passservice.domain.pass.enums.IssuanceStatus;
import com.doubleo.passservice.domain.pass.enums.VisitCategory;
import com.doubleo.passservice.domain.pass.repository.PassAreaRepository;
import com.doubleo.passservice.domain.pass.repository.PassRepository;
import com.doubleo.passservice.global.exception.CommonException;
import com.doubleo.passservice.global.exception.errorcode.PassErrorCode;
import com.doubleo.passservice.grpc.client.*;
import com.doubleo.patientservice.domain.guardian.grpc.server.GuardianResponse;
import com.doubleo.patientservice.domain.patient.grpc.server.PatientResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PassServiceImpl implements PassService {

    public static final String GUARDIAN_APPLY_NOTIFICATION_TITLE = "보호자 신청";
    public static final String GUARDIAN_APPLY_NOTIFICATION_CONTENT = "%s 님이 보호자 신청 요청을 하였습니다.";
    public static final String GUARDIAN_APPROVED_NOTIFICATION_TITLE = "보호자 신청 승인";
    public static final String GUARDIAN_APPROVED_NOTIFICATION_CONTENT = "%s 님의 보호자 신청이 승인되었습니다.";
    public static final String GUARDIAN_REJECTED_NOTIFICATION_TITLE = "보호자 신청 거절";
    public static final String GUARDIAN_REJECTED_NOTIFICATION_CONTENT = "%s 님의 보호자 신청이 거절되었습니다.";

    private final PassRepository passRepository;
    private final PassAreaRepository passAreaRepository;
    private final MemberClient memberClient;
    private final AreaClient areaClient;
    private final PatientClient patientClient;
    private final GuardianClient guardianClient;
    private final LogClient logClient;
    private final FcmService fcmService;

    @Override
    public List<MemberPassInfoResponse> getAllMemberPassInfo(Long memberId) {
        List<Pass> passes = passRepository.findAllByMemberId(memberId);
        List<MemberPassInfoResponse> responses = new ArrayList<>();
        for (Pass pass : passes) {
            List<PassArea> passAreas = passAreaRepository.findAllByPass(pass);
            List<String> areaNames = new ArrayList<>();
            for (PassArea passArea : passAreas) {
                String areaName =
                        areaClient
                                .getAreaFullNameByCode(
                                        passArea.getTenantId(), passArea.getAreaCode())
                                .getAreaFullName();
                areaNames.add(areaName);
            }
            Long patientId = pass.getPatientId();
            PatientResponse patient = patientClient.getPatientById(patientId);
            List<GuardianInfo> guardians;
            if (pass.getVisitCategory() == VisitCategory.PATIENT) {
                guardians =
                        guardianClient.getPatientGuardianList(patientId).getGuardiansList().stream()
                                .map(
                                        res ->
                                                new GuardianInfo(
                                                        res.getGuardianName(),
                                                        res.getGuardianContact()))
                                .toList();
            } else {
                guardians = null;
            }
            MemberPassInfoResponse response =
                    new MemberPassInfoResponse(
                            pass.getId(),
                            pass.getMemberId(),
                            pass.getHospitalId(),
                            areaNames,
                            pass.getVisitCategory(),
                            patientId,
                            patient.getName(),
                            guardians,
                            pass.getIssuanceStatus(),
                            pass.getStartAt(),
                            pass.getExpiredAt());
            responses.add(response);
        }
        return responses;
    }

    @Override
    public PassCreateResponse createPatientPass(
            Long memberId, Long hospitalId, String tenantId, String startAtInput) {
        LocalDateTime startAt = parseDate(startAtInput);
        MemberResponse member = memberClient.getMemberById(memberId);

        PatientResponse patient =
                patientClient.getPatientByNameAndRegNo(
                        tenantId, member.getMemberName(), member.getMemberRegNo());

        return createPass(
                memberId,
                hospitalId,
                patient.getPatientId(),
                tenantId,
                startAt,
                startAt.plusDays(1),
                VisitCategory.PATIENT,
                IssuanceStatus.ISSUED);
    }

    @Override
    public PassCreateResponse createGuardianPass(
            Long memberId,
            Long hospitalId,
            String tenantId,
            String patientCode,
            String startAtInput) {
        LocalDateTime startAt = parseDate(startAtInput);
        MemberResponse member = memberClient.getMemberById(memberId);
        String memberName = member.getMemberName();
        String memberContact = member.getMemberContact();

        PatientResponse patient = patientClient.getPatientByPatientCode(tenantId, patientCode);
        Long patientId = patient.getPatientId();

        List<GuardianResponse> guardians =
                guardianClient.getPatientGuardianList(patientId).getGuardiansList();
        for (GuardianResponse guardian : guardians) {
            if (guardian.getGuardianName().equals(memberName)
                    && guardian.getGuardianContact().equals(memberContact)) {
                return createPass(
                        memberId,
                        hospitalId,
                        patientId,
                        tenantId,
                        startAt,
                        startAt.plusDays(1),
                        VisitCategory.GUARDIAN,
                        IssuanceStatus.ISSUED);
            }
        }
        return createPass(
                memberId,
                hospitalId,
                patientId,
                tenantId,
                startAt,
                startAt.plusDays(1),
                VisitCategory.GUARDIAN,
                IssuanceStatus.PENDING);
    }

    @Override
    public void deletePass(Long passId) {
        passRepository.deleteById(passId);
    }

    @Override
    public Page<PendingPassResponse> getPendingPassList(String tenantId, Pageable pageable) {
        Page<Pass> passes =
                passRepository.findAllByTenantIdAndIssuanceStatus(
                        tenantId, IssuanceStatus.PENDING, pageable);
        return passes.map(
                pass -> {
                    MemberResponse member = memberClient.getMemberById(pass.getMemberId());
                    PatientResponse patient = patientClient.getPatientById(pass.getPatientId());

                    return new PendingPassResponse(
                            pass.getId(),
                            member.getMemberId(),
                            patient.getPatientCode(),
                            patient.getName(),
                            member.getMemberName(),
                            member.getMemberContact(),
                            pass.getCreatedDt(),
                            pass.getStartAt(),
                            pass.getExpiredAt());
                });
    }

    @Override
    public PassCreateResponse createGuardianAndUpdatePassStatus(
            Long passId, IssuanceStatus issuanceStatus) {
        Optional<Pass> optionalPass = passRepository.findById(passId);
        if (optionalPass.isPresent()) {
            Pass pass = optionalPass.get();

            MemberResponse member = memberClient.getMemberById(pass.getMemberId());
            PatientResponse patient = patientClient.getPatientById(pass.getPatientId());
            MemberResponse patientMember = null;
            try {
                patientMember =
                        memberClient.getMemberByNameAndRegNo(patient.getName(), patient.getRegNo());
            } catch (Exception e) {
                log.warn("환자 멤버가 존재하지 않아 알림을 생략합니다.");
            }

            try {
                // TODO: 실제 connection id 가져올 부분
                String connectionId = "did:peer:example123";
                pass.updateConnectionId(connectionId);
            } catch (Exception e) {
                log.warn("DID Connection ID를 가져오는 데 실패했습니다: {}", e.getMessage());
            }

            pass.updateStatus(issuanceStatus);
            pass = passRepository.save(pass);

            if (issuanceStatus == IssuanceStatus.ISSUED) {
                guardianClient.createGuardian(
                        pass.getTenantId(),
                        pass.getPatientId(),
                        member.getMemberName(),
                        member.getMemberContact());
                List<String> areaCodes =
                        passAreaRepository.findAllByPass(pass).stream()
                                .map(PassArea::getAreaCode)
                                .toList();
                logClient.createIssuedLog(
                        pass.getTenantId(),
                        pass.getMemberId(),
                        member.getMemberName(),
                        member.getMemberContact(),
                        pass.getId(),
                        pass.getStartAt(),
                        pass.getExpiredAt(),
                        pass.getVisitCategory(),
                        areaCodes);
                fcmService.sendNotification(
                        new FcmSendRequest(
                                member.getMemberId(),
                                member.getFcmToken(),
                                GUARDIAN_APPROVED_NOTIFICATION_TITLE,
                                String.format(
                                        GUARDIAN_APPROVED_NOTIFICATION_CONTENT,
                                        member.getMemberName())));
                if (patientMember != null) {
                    fcmService.sendNotification(
                            new FcmSendRequest(
                                    patientMember.getMemberId(),
                                    patientMember.getFcmToken(),
                                    GUARDIAN_APPROVED_NOTIFICATION_TITLE,
                                    String.format(
                                            GUARDIAN_APPROVED_NOTIFICATION_CONTENT,
                                            member.getMemberName())));
                }
            } else if (issuanceStatus == IssuanceStatus.REJECTED) {
                fcmService.sendNotification(
                        new FcmSendRequest(
                                member.getMemberId(),
                                member.getFcmToken(),
                                GUARDIAN_REJECTED_NOTIFICATION_TITLE,
                                String.format(
                                        GUARDIAN_REJECTED_NOTIFICATION_CONTENT,
                                        member.getMemberName())));
                if (patientMember != null) {
                    fcmService.sendNotification(
                            new FcmSendRequest(
                                    patientMember.getMemberId(),
                                    patientMember.getFcmToken(),
                                    GUARDIAN_REJECTED_NOTIFICATION_TITLE,
                                    String.format(
                                            GUARDIAN_REJECTED_NOTIFICATION_CONTENT,
                                            member.getMemberName())));
                }
            }
            return new PassCreateResponse(pass.getId());
        } else {
            throw new CommonException(PassErrorCode.PASS_NOT_FOUND);
        }
    }

    private PassCreateResponse createPass(
            Long memberId,
            Long hospitalId,
            Long patientId,
            String tenantId,
            LocalDateTime startAt,
            LocalDateTime expiredAt,
            VisitCategory visitCategory,
            IssuanceStatus status) {
        MemberResponse member = memberClient.getMemberById(memberId);

        PatientResponse patient = patientClient.getPatientById(patientId);

        String connectionId = null;

        List<AreaResponse> areas =
                patient.getAreasList().stream().map(areaClient::getAreaById).toList();
        List<String> areaCodes = areas.stream().map(AreaResponse::getAreaCode).toList();

        try {
            // TODO: 실제 connection id 가져올 부분
            connectionId = "did:peer:example123";
        } catch (Exception e) {
            log.warn("DID Connection ID를 가져오는 데 실패했습니다: {}", e.getMessage());
        }

        Pass pass =
                Pass.createPass(
                        tenantId,
                        memberId,
                        hospitalId,
                        startAt,
                        expiredAt,
                        patientId,
                        visitCategory,
                        status,
                        connectionId);
        passRepository.save(pass);

        List<PassArea> passAreas =
                areaCodes.stream()
                        .map(code -> PassArea.createPassArea(tenantId, pass, code))
                        .toList();
        passAreaRepository.saveAll(passAreas);

        if (visitCategory == VisitCategory.GUARDIAN) {
            MemberResponse patientMember = null;
            try {
                patientMember =
                        memberClient.getMemberByNameAndRegNo(patient.getName(), patient.getRegNo());
            } catch (Exception e) {
                log.warn("환자 멤버가 존재하지 않아 알림을 생략합니다.");
            }
            if (patientMember != null) {
                fcmService.sendNotification(
                        new FcmSendRequest(
                                patientMember.getMemberId(),
                                patientMember.getFcmToken(),
                                GUARDIAN_APPLY_NOTIFICATION_TITLE,
                                String.format(
                                        GUARDIAN_APPLY_NOTIFICATION_CONTENT,
                                        member.getMemberName())));
            }
        }

        if (status == IssuanceStatus.ISSUED) {
            logClient.createIssuedLog(
                    tenantId,
                    memberId,
                    member.getMemberName(),
                    member.getMemberContact(),
                    pass.getId(),
                    startAt,
                    expiredAt,
                    visitCategory,
                    areaCodes);
        }
        return new PassCreateResponse(pass.getId());
    }

    private LocalDateTime parseDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
    }
}
