package com.doubleo.passservice.domain.pass.dto.response;

import com.doubleo.passservice.domain.pass.dto.GuardianInfo;
import com.doubleo.passservice.domain.pass.enums.IssuanceStatus;
import com.doubleo.passservice.domain.pass.enums.VisitCategory;
import java.time.LocalDateTime;
import java.util.List;

public record MemberPassInfoResponse(
        Long passId,
        Long memberId,
        Long hospitalId,
        List<String> accessAreaNames,
        VisitCategory visitCategory,
        Long patientId,
        String patientName,
        List<GuardianInfo> guardians,
        IssuanceStatus issuanceStatus,
        LocalDateTime startedAt,
        LocalDateTime expiredAt) {}
