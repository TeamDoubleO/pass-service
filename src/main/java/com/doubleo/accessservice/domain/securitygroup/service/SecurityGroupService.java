package com.doubleo.accessservice.domain.securitygroup.service;

import com.doubleo.accessservice.domain.securitygroup.dto.GroupAreaDto;
import com.doubleo.accessservice.domain.securitygroup.dto.GroupMemberDto;
import com.doubleo.accessservice.domain.securitygroup.dto.request.SecurityGroupRequest;
import com.doubleo.accessservice.domain.securitygroup.dto.response.SecurityGroupResponse;
import java.util.List;

public interface SecurityGroupService {
    SecurityGroupResponse createSecurityGroup(SecurityGroupRequest request);

    SecurityGroupResponse updateSecurityGroup(Long groupId, SecurityGroupRequest request);

    void deleteSecurityGroup(Long groupId);

    List<SecurityGroupResponse> getAllSecurityGroups();

    GroupMemberDto addGroupMember(GroupMemberDto groupMemberDto);

    void deleteGroupMember(GroupMemberDto groupMemberDto);

    List<GroupMemberDto> getAllGroupMembers(Long groupId);

    GroupAreaDto addGroupArea(GroupAreaDto groupAreaDto);

    void deleteGroupArea(GroupAreaDto groupAreaDto);

    List<GroupAreaDto> getAllGroupAreas(Long groupId);
}
