package com.doubleo.accessservice.domain.securitygroup.service;

import com.doubleo.accessservice.domain.securitygroup.dto.GroupAreaDto;
import com.doubleo.accessservice.domain.securitygroup.dto.GroupMemberDto;
import com.doubleo.accessservice.domain.securitygroup.dto.SecurityGroupDto;

import java.util.List;

public interface SecurityGroupService {
    SecurityGroupDto createSecurityGroup(SecurityGroupDto securityGroupDto);
    SecurityGroupDto updateSecurityGroup(SecurityGroupDto securityGroupDto);
    void deleteSecurityGroup(Long groupId);
    List<SecurityGroupDto> getAllSecurityGroups();

    GroupMemberDto addGroupMember(GroupMemberDto groupMemberDto);
    void deleteGroupMember(GroupMemberDto groupMemberDto);
    List<GroupMemberDto> getAllGroupMembers(Long groupId);

    GroupAreaDto addGroupArea(GroupAreaDto groupAreaDto);
    void deleteGroupArea(GroupAreaDto groupAreaDto);
    List<GroupAreaDto> getAllGroupAreas(Long groupId);
}
