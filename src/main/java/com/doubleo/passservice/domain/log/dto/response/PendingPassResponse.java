package com.doubleo.passservice.domain.log.dto.response;

import java.time.LocalDateTime;

public record PendingPassResponse(
        Long passId,
        String patientCode,
        String guardianName,
        String guardianContact,
        LocalDateTime startAt,
        LocalDateTime expiredAt) {}
