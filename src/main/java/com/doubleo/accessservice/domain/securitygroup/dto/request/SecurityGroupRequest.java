package com.doubleo.accessservice.domain.securitygroup.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SecurityGroupRequest(
        @Schema(description = "그룹 이름", example = "팀 1") @NotNull String groupName,
        @Schema(description = "그룹 설명", example = "프로젝트 1을 위한 보안 그룹입니다.")
                @Size(max = 100, message = "그룹 설명은 최대 100자까지 입력 가능합니다.")
                String description) {}
