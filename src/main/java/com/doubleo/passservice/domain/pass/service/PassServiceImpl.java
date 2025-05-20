package com.doubleo.passservice.domain.pass.service;

import com.doubleo.memberservice.domain.member.grpc.server.MemberResponse;
import com.doubleo.passservice.domain.pass.domain.Pass;
import com.doubleo.passservice.domain.pass.domain.PassArea;
import com.doubleo.passservice.domain.pass.dto.response.MemberPassInfoResponse;
import com.doubleo.passservice.domain.pass.dto.response.PassCreateResponse;
import com.doubleo.passservice.domain.pass.repository.PassAreaRepository;
import com.doubleo.passservice.domain.pass.repository.PassRepository;
import com.doubleo.passservice.grpc.client.AreaClient;
import com.doubleo.passservice.grpc.client.GuardianClient;
import com.doubleo.passservice.grpc.client.MemberClient;
import com.doubleo.passservice.grpc.client.PatientClient;
import com.doubleo.patientservice.domain.patient.grpc.server.PatientResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PassServiceImpl implements PassService {

    private final PassRepository passRepository;
    private final PassAreaRepository passAreaRepository;
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
        PatientResponse patient = patientClient.getPatientById(memberId);
        // member 와 patient 비교
        // 발급
        return null;
    }

    @Override
    public PassCreateResponse createGuardianPass(
            Long memberId,
            Long hospitalId,
            String tenantId,
            Long patientId,
            LocalDateTime startAt) {
        return null;
    }

    private PassCreateResponse createPass(
            Long memberId, Long hospitalId, String tenantId, LocalDateTime startAt) {
        return null;
    }
}
