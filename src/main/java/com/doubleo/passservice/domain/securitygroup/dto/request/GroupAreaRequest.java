package com.doubleo.passservice.domain.securitygroup.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record GroupAreaRequest(
        @Schema(description = "구역 ID", example = "1") @NotNull Long areaId) {}
