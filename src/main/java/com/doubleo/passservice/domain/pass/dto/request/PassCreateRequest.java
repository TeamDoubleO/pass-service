package com.doubleo.passservice.domain.pass.dto.request;

import com.doubleo.passservice.domain.pass.enums.VisitCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public record PassCreateRequest(
        @Schema(description = "방문자 구분", example = "PATIENT") @NotBlank VisitCategory visitCategory,
        @Schema(description = "환자 번호", example = "1") Long patientId,
        @Schema(description = "시작 시간", example = "2007-12-03T10:15:30") @NotBlank
                LocalDateTime startAt) {}
