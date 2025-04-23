package com.doubleo.accessservice.domain.securitygroup.service;

import com.doubleo.accessservice.domain.securitygroup.dto.request.GroupAreaRequest;
import com.doubleo.accessservice.domain.securitygroup.dto.request.GroupMemberRequest;
import com.doubleo.accessservice.domain.securitygroup.dto.request.SecurityGroupRequest;
import com.doubleo.accessservice.domain.securitygroup.dto.response.GroupAreaResponse;
import com.doubleo.accessservice.domain.securitygroup.dto.response.GroupMemberResponse;
import com.doubleo.accessservice.domain.securitygroup.dto.response.SecurityGroupResponse;
import java.util.List;

public interface SecurityGroupService {
    SecurityGroupResponse createSecurityGroup(SecurityGroupRequest request);

    SecurityGroupResponse updateSecurityGroup(Long groupId, SecurityGroupRequest request);

    void deleteSecurityGroup(Long groupId);

    List<SecurityGroupResponse> getAllSecurityGroups();

    GroupMemberResponse addGroupMember(Long groupId, GroupMemberRequest request);

    void deleteGroupMember(Long groupId, GroupMemberRequest request);

    List<GroupMemberResponse> getAllGroupMembers(Long groupId);

    GroupAreaResponse addGroupArea(Long groupId, GroupAreaRequest request);

    void deleteGroupArea(Long groupId, GroupAreaRequest request);

    List<GroupAreaResponse> getAllGroupAreas(Long groupId);
}
