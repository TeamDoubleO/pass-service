package com.doubleo.passservice.domain.log.dto.response;

import com.doubleo.passservice.domain.pass.enums.VisitCategory;
import java.time.LocalDateTime;
import java.util.List;

public record IssuedLogResponse(
        Long memberId,
        String memberName,
        Long passId,
        String areaCode,
        List<String> areaName,
        LocalDateTime startAt,
        LocalDateTime expiredAt,
        VisitCategory visitCategory) {}
