package com.doubleo.accessservice.domain.securitygroup.dto.response;

import com.doubleo.accessservice.domain.securitygroup.domain.SecurityGroup;
import io.swagger.v3.oas.annotations.media.Schema;

public record SecurityGroupResponse(
        @Schema(description = "보안그룹 아이디", example = "1") Long groupId,
        @Schema(description = "보안그룹 이름", example = "팀 1") String groupName,
        @Schema(description = "보안그룹 설명", example = "프로젝트 1을 위한 그룹입니다.") String description) {
    public static SecurityGroupResponse from(SecurityGroup securityGroup) {
        return new SecurityGroupResponse(
                securityGroup.getId(),
                securityGroup.getGroupName(),
                securityGroup.getDescription());
    }
}
