package com.doubleo.accessservice.domain.securitygroup.dto.response;

import com.doubleo.accessservice.domain.securitygroup.domain.GroupArea;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record GroupAreaResponse(
        @Schema(description = "그룹 ID", example = "1") @NotNull Long groupId,
        @Schema(description = "구역 ID", example = "1") @NotNull Long areaId) {
    public static GroupAreaResponse from(GroupArea groupArea) {
        return new GroupAreaResponse(groupArea.getSecurityGroup().getId(), groupArea.getAreaId());
    }
}
