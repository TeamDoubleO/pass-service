package com.doubleo.passservice.domain.securitygroup.dto.response;

import com.doubleo.passservice.domain.securitygroup.domain.GroupMember;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record GroupMemberResponse(
        @Schema(description = "그룹 ID", example = "1") @NotNull Long groupId,
        @Schema(description = "사원 ID", example = "1") @NotNull Long employeeId) {
    public static GroupMemberResponse from(GroupMember groupMember) {
        return new GroupMemberResponse(
                groupMember.getSecurityGroup().getId(), groupMember.getEmployeeId());
    }
}
