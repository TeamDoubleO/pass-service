package com.doubleo.passservice.domain.securitygroup.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record GroupMemberRequest(
        @Schema(description = "사원 ID", example = "1") @NotNull Long employeeId) {}
