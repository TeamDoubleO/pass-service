package com.doubleo.passservice.domain.pass.dto.request;

import com.doubleo.passservice.domain.pass.enums.VisitCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record PassCreateRequest(
        @Schema(description = "방문자 구분", example = "PATIENT") @NotBlank VisitCategory visitCategory,
        @Schema(description = "환자 번호", example = "1") Long patientId) {}
