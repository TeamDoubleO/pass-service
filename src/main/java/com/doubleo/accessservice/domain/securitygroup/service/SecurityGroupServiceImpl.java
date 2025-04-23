package com.doubleo.accessservice.domain.securitygroup.service;

import com.doubleo.accessservice.domain.securitygroup.domain.GroupArea;
import com.doubleo.accessservice.domain.securitygroup.domain.GroupMember;
import com.doubleo.accessservice.domain.securitygroup.domain.SecurityGroup;
import com.doubleo.accessservice.domain.securitygroup.dto.GroupAreaDto;
import com.doubleo.accessservice.domain.securitygroup.dto.GroupMemberDto;
import com.doubleo.accessservice.domain.securitygroup.dto.SecurityGroupDto;
import com.doubleo.accessservice.domain.securitygroup.repository.GroupAreaRepository;
import com.doubleo.accessservice.domain.securitygroup.repository.GroupMemberRepository;
import com.doubleo.accessservice.domain.securitygroup.repository.SecurityGroupRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class SecurityGroupServiceImpl implements SecurityGroupService {
    private final GroupAreaRepository groupAreaRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final SecurityGroupRepository securityGroupRepository;

    private final ModelMapper modelMapper;

    @Override
    public SecurityGroupDto createSecurityGroup(SecurityGroupDto securityGroupDto) {
        SecurityGroup securityGroup =
                SecurityGroup.createSecurityGroup(
                        securityGroupDto.getGroupName(), securityGroupDto.getDescription());
        securityGroupRepository.save(securityGroup);
        securityGroupDto.setId(securityGroup.getId());
        return securityGroupDto;
    }

    @Override
    public SecurityGroupDto updateSecurityGroup(SecurityGroupDto securityGroupDto) {
        SecurityGroup securityGroup =
                securityGroupRepository.findByGroupId(securityGroupDto.getId());
        securityGroup.updateSecurityGroup(
                securityGroupDto.getGroupName(), securityGroupDto.getDescription());
        securityGroupRepository.save(securityGroup);
        return securityGroupDto;
    }

    @Override
    public void deleteSecurityGroup(Long groupId) {
        securityGroupRepository.deleteById(groupId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<SecurityGroupDto> getAllSecurityGroups() {
        return securityGroupRepository.findAll().stream()
                .map(securityGroup -> modelMapper.map(securityGroup, SecurityGroupDto.class))
                .toList();
    }

    @Override
    public GroupMemberDto addGroupMember(GroupMemberDto groupMemberDto) {
        SecurityGroup securityGroup =
                securityGroupRepository.findByGroupId(groupMemberDto.getGroupId());
        GroupMember groupMember =
                GroupMember.createGroupMember(securityGroup, groupMemberDto.getEmployeeId());
        groupMemberRepository.save(groupMember);
        return groupMemberDto;
    }

    @Override
    public void deleteGroupMember(GroupMemberDto groupMemberDto) {
        groupMemberRepository.deleteBySecurityGroup_IdAndEmployeeId(
                groupMemberDto.getGroupId(), groupMemberDto.getEmployeeId());
    }

    @Transactional(readOnly = true)
    @Override
    public List<GroupMemberDto> getAllGroupMembers(Long groupId) {
        return groupMemberRepository.findAllBySecurityGroup_Id(groupId).stream()
                .map(member -> modelMapper.map(member, GroupMemberDto.class))
                .toList();
    }

    @Override
    public GroupAreaDto addGroupArea(GroupAreaDto groupAreaDto) {
        SecurityGroup securityGroup =
                securityGroupRepository.findByGroupId(groupAreaDto.getGroupId());
        GroupArea groupArea = GroupArea.createGroupArea(securityGroup, groupAreaDto.getAreaId());
        groupAreaRepository.save(groupArea);
        return groupAreaDto;
    }

    @Override
    public void deleteGroupArea(GroupAreaDto groupAreaDto) {
        groupAreaRepository.deleteBySecurityGroup_IdAndAreaId(
                groupAreaDto.getGroupId(), groupAreaDto.getAreaId());
    }

    @Transactional(readOnly = true)
    @Override
    public List<GroupAreaDto> getAllGroupAreas(Long groupId) {
        return groupAreaRepository.findAllBySecurityGroup_Id(groupId).stream()
                .map(area -> modelMapper.map(area, GroupAreaDto.class))
                .toList();
    }
}
