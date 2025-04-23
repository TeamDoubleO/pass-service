package com.doubleo.accessservice.domain.securitygroup.service;

import com.doubleo.accessservice.domain.securitygroup.domain.GroupArea;
import com.doubleo.accessservice.domain.securitygroup.domain.GroupMember;
import com.doubleo.accessservice.domain.securitygroup.domain.SecurityGroup;
import com.doubleo.accessservice.domain.securitygroup.dto.request.GroupAreaRequest;
import com.doubleo.accessservice.domain.securitygroup.dto.request.GroupMemberRequest;
import com.doubleo.accessservice.domain.securitygroup.dto.request.SecurityGroupRequest;
import com.doubleo.accessservice.domain.securitygroup.dto.response.GroupAreaResponse;
import com.doubleo.accessservice.domain.securitygroup.dto.response.GroupMemberResponse;
import com.doubleo.accessservice.domain.securitygroup.dto.response.SecurityGroupResponse;
import com.doubleo.accessservice.domain.securitygroup.repository.GroupAreaRepository;
import com.doubleo.accessservice.domain.securitygroup.repository.GroupMemberRepository;
import com.doubleo.accessservice.domain.securitygroup.repository.SecurityGroupRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SecurityGroupServiceImpl implements SecurityGroupService {
    private final GroupAreaRepository groupAreaRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final SecurityGroupRepository securityGroupRepository;

    private final ModelMapper modelMapper;

    @Override
    public SecurityGroupResponse createSecurityGroup(SecurityGroupRequest request) {
        if (securityGroupRepository.existsByGroupName(request.groupName())) {
            throw new IllegalArgumentException("Group name already exists");
        }
        SecurityGroup securityGroup =
                SecurityGroup.createSecurityGroup(request.groupName(), request.description());
        securityGroupRepository.save(securityGroup);
        return SecurityGroupResponse.from(securityGroup);
    }

    @Override
    public SecurityGroupResponse updateSecurityGroup(Long groupId, SecurityGroupRequest request) {
        Optional<SecurityGroup> securityGroup = securityGroupRepository.findById(groupId);
        if (securityGroup.isEmpty()) {
            throw new IllegalArgumentException("Group not found");
        }
        SecurityGroup securityGroupEntity = securityGroup.get();
        if (securityGroupRepository.existsByGroupName(request.groupName())) {
            throw new IllegalArgumentException("Group name already exists");
        }
        securityGroupEntity.updateSecurityGroup(request.groupName(), request.description());
        securityGroupRepository.save(securityGroupEntity);
        return SecurityGroupResponse.from(securityGroupEntity);
    }

    @Override
    public void deleteSecurityGroup(Long groupId) {
        securityGroupRepository.deleteById(groupId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<SecurityGroupResponse> getAllSecurityGroups() {
        return securityGroupRepository.findAll().stream().map(SecurityGroupResponse::from).toList();
    }

    @Override
    public GroupMemberResponse addGroupMember(Long groupId, GroupMemberRequest request) {
        Optional<SecurityGroup> securityGroup = securityGroupRepository.findById(groupId);
        if (securityGroup.isEmpty()) {
            throw new IllegalArgumentException("Group not found");
        }
        SecurityGroup securityGroupEntity = securityGroup.get();
        if (groupMemberRepository.existsBySecurityGroup_IdAndEmployeeId(
                groupId, request.employeeId())) {
            throw new IllegalArgumentException("Employee id already exists");
        }
        GroupMember groupMember =
                GroupMember.createGroupMember(securityGroupEntity, request.employeeId());
        groupMemberRepository.save(groupMember);
        return GroupMemberResponse.from(groupMember);
    }

    @Override
    public void deleteGroupMember(Long groupId, GroupMemberRequest request) {
        groupMemberRepository.deleteBySecurityGroup_IdAndEmployeeId(groupId, request.employeeId());
    }

    @Transactional(readOnly = true)
    @Override
    public List<GroupMemberResponse> getAllGroupMembers(Long groupId) {
        return groupMemberRepository.findAllBySecurityGroup_Id(groupId).stream()
                .map(GroupMemberResponse::from)
                .toList();
    }

    @Override
    public GroupAreaResponse addGroupArea(Long groupId, GroupAreaRequest request) {
        Optional<SecurityGroup> securityGroup = securityGroupRepository.findById(groupId);
        if (securityGroup.isEmpty()) {
            throw new IllegalArgumentException("Group not found");
        }
        SecurityGroup securityGroupEntity = securityGroup.get();
        if (groupAreaRepository.existsBySecurityGroup_IdAndAreaId(groupId, request.areaId())) {
            throw new IllegalArgumentException("Area id already exists");
        }
        GroupArea groupArea = GroupArea.createGroupArea(securityGroupEntity, request.areaId());
        groupAreaRepository.save(groupArea);
        return GroupAreaResponse.from(groupArea);
    }

    @Override
    public void deleteGroupArea(Long groupId, GroupAreaRequest request) {
        groupAreaRepository.deleteBySecurityGroup_IdAndAreaId(groupId, request.areaId());
    }

    @Transactional(readOnly = true)
    @Override
    public List<GroupAreaResponse> getAllGroupAreas(Long groupId) {
        return groupAreaRepository.findAllBySecurityGroup_Id(groupId).stream()
                .map(GroupAreaResponse::from)
                .toList();
    }
}
