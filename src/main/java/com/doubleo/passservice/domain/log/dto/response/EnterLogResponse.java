package com.doubleo.passservice.domain.log.dto.response;

import java.time.LocalDateTime;

public record EnterLogResponse(
        Long areaId,
        String areaName,
        Long memberId,
        String memberName,
        Long passId,
        LocalDateTime createdDt) {}
