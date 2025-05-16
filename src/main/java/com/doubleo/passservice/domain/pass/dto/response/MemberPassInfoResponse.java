package com.doubleo.passservice.domain.pass.dto.response;

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
        LocalDateTime startedAt,
        LocalDateTime expiredAt) {}
