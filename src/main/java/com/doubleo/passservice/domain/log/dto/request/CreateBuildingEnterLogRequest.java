package com.doubleo.passservice.domain.log.dto.request;

import com.doubleo.passservice.domain.log.domain.Direction;

public record CreateBuildingEnterLogRequest(
        String tenantId,
        Long buildingId,
        Long memberId,
        String memberName,
        Long passId,
        Direction direction) {}
