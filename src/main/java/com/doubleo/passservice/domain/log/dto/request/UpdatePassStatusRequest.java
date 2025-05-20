package com.doubleo.passservice.domain.log.dto.request;

import com.doubleo.passservice.domain.pass.enums.IssuanceStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UpdatePassStatusRequest(
        @Schema(description = "pass id", example = "1") @NotBlank Long passId,
        @Schema(description = "출입증 상태", example = "PENDING") @NotBlank
                IssuanceStatus issuanceStatus) {}
