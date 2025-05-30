package com.doubleo.passservice.domain.pass.service;

import com.doubleo.hospitalservice.domain.area.grpc.server.AreaResponse;
import com.doubleo.memberservice.domain.member.grpc.server.MemberResponse;
import com.doubleo.passservice.domain.log.domain.IssuedLog;
import com.doubleo.passservice.domain.log.domain.IssuedLogArea;
import com.doubleo.passservice.domain.log.dto.response.PendingPassResponse;
import com.doubleo.passservice.domain.log.repository.IssuedLogAreaRepository;
import com.doubleo.passservice.domain.log.repository.IssuedLogRepository;
import com.doubleo.passservice.domain.pass.domain.Pass;
import com.doubleo.passservice.domain.pass.domain.PassArea;
import com.doubleo.passservice.domain.pass.dto.response.MemberPassInfoResponse;
import com.doubleo.passservice.domain.pass.dto.response.PassCreateResponse;
import com.doubleo.passservice.domain.pass.enums.IssuanceStatus;
import com.doubleo.passservice.domain.pass.enums.VisitCategory;
import com.doubleo.passservice.domain.pass.repository.PassAreaRepository;
import com.doubleo.passservice.domain.pass.repository.PassRepository;
import com.doubleo.passservice.global.exception.CommonException;
import com.doubleo.passservice.global.exception.errorcode.PassErrorCode;
import com.doubleo.passservice.grpc.client.AreaClient;
import com.doubleo.passservice.grpc.client.GuardianClient;
import com.doubleo.passservice.grpc.client.MemberClient;
import com.doubleo.passservice.grpc.client.PatientClient;
import com.doubleo.patientservice.domain.guardian.grpc.server.GuardianResponse;
import com.doubleo.patientservice.domain.patient.grpc.server.PatientResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PassServiceImpl implements PassService {

    private final PassRepository passRepository;
    private final PassAreaRepository passAreaRepository;
    private final IssuedLogRepository issuedLogRepository;
    private final IssuedLogAreaRepository issuedLogAreaRepository;
    private final MemberClient memberClient;
    private final AreaClient areaClient;
    private final PatientClient patientClient;
    private final GuardianClient guardianClient;

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
            MemberPassInfoResponse response =
                    new MemberPassInfoResponse(
                            pass.getId(),
                            pass.getMemberId(),
                            pass.getHospitalId(),
                            areaNames,
                            pass.getVisitCategory(),
                            pass.getPatientId(),
                            pass.getIssuanceStatus(),
                            pass.getStartAt(),
                            pass.getExpiredAt());
            responses.add(response);
        }
        return responses;
    }

    @Override
    public PassCreateResponse createPatientPass(
            Long memberId, Long hospitalId, String tenantId, LocalDateTime startAt) {
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
            LocalDateTime startAt) {
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

            if (issuanceStatus == IssuanceStatus.ISSUED) {
                guardianClient.createGuardian(
                        pass.getTenantId(),
                        pass.getPatientId(),
                        member.getMemberName(),
                        member.getMemberContact());
            }
            pass.updateStatus(issuanceStatus);
            pass = passRepository.save(pass);
            List<String> areaCodes =
                    passAreaRepository.findAllByPass(pass).stream()
                            .map(PassArea::getAreaCode)
                            .toList();
            createIssuedLog(
                    pass.getTenantId(),
                    pass.getMemberId(),
                    member.getMemberName(),
                    member.getMemberContact(),
                    pass.getId(),
                    pass.getStartAt(),
                    pass.getExpiredAt(),
                    pass.getVisitCategory(),
                    areaCodes);
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
        PatientResponse patient = patientClient.getPatientById(patientId);

        List<AreaResponse> areas =
                patient.getAreasList().stream().map(areaClient::getAreaById).toList();
        List<String> areaCodes = areas.stream().map(AreaResponse::getAreaCode).toList();

        Pass pass =
                Pass.createPass(
                        tenantId,
                        memberId,
                        hospitalId,
                        startAt,
                        expiredAt,
                        patientId,
                        visitCategory,
                        status);
        passRepository.save(pass);

        List<PassArea> passAreas =
                areaCodes.stream()
                        .map(code -> PassArea.createPassArea(tenantId, pass, code))
                        .toList();
        passAreaRepository.saveAll(passAreas);

        if (status == IssuanceStatus.ISSUED) {
            MemberResponse member = memberClient.getMemberById(memberId);
            createIssuedLog(
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

    private void createIssuedLog(
            String tenantId,
            Long memberId,
            String memberName,
            String memberContact,
            Long passId,
            LocalDateTime startAt,
            LocalDateTime expiredAt,
            VisitCategory visitCategory,
            List<String> areaCodes) {
        IssuedLog issuedLog =
                issuedLogRepository.save(
                        IssuedLog.createIssuedLog(
                                tenantId,
                                memberId,
                                memberName,
                                memberContact,
                                passId,
                                startAt,
                                expiredAt,
                                visitCategory));
        List<IssuedLogArea> logAreas =
                areaCodes.stream()
                        .map(code -> IssuedLogArea.createIssuedLogArea(tenantId, issuedLog, code))
                        .toList();

        issuedLogAreaRepository.saveAll(logAreas);
    }
}
