package com.doubleo.passservice.domain.pass.service;

import com.doubleo.passservice.domain.pass.dto.response.MemberPassInfoResponse;
import com.doubleo.passservice.domain.pass.dto.response.PassCreateResponse;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

public interface PassService {
    List<MemberPassInfoResponse> getAllMemberPassInfo(Long memberId);

    PassCreateResponse createPatientPass(
            Long memberId, Long hospitalId, String tenantId, @NotBlank LocalDateTime startAt);

    PassCreateResponse createGuardianPass(
            Long memberId,
            Long hospitalId,
            String tenantId,
            String patientCode,
            @NotBlank LocalDateTime startAt);
}
