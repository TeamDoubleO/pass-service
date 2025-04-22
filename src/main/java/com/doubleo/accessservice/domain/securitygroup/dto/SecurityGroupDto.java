package com.doubleo.accessservice.domain.securitygroup.dto;

import lombok.Data;

@Data
public class SecurityGroupDto {
    private Long id;
    private String groupName;
    private String description;
}