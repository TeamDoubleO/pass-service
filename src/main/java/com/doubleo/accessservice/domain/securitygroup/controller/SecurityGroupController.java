package com.doubleo.accessservice.domain.securitygroup.controller;

import com.doubleo.accessservice.domain.securitygroup.dto.GroupAreaDto;
import com.doubleo.accessservice.domain.securitygroup.dto.GroupMemberDto;
import com.doubleo.accessservice.domain.securitygroup.dto.SecurityGroupDto;
import com.doubleo.accessservice.domain.securitygroup.service.SecurityGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/access/group")
@AllArgsConstructor
public class SecurityGroupController {

    private final SecurityGroupService securityGroupService;

    @Operation(summary = "Security Group create API", description = "Security Group을 생성하기 위한 API")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST")
    })
    @PostMapping
    public ResponseEntity<SecurityGroupDto> createSecurityGroup(@RequestBody SecurityGroupDto securityGroupDto) {
        return ResponseEntity.ok(securityGroupService.createSecurityGroup(securityGroupDto));
    }

    @Operation(summary = "Security Group update API", description = "Security Group을 수정하기 위한 API")
    @PatchMapping("/{groupId}")
    public ResponseEntity<SecurityGroupDto> updateSecurityGroup(
            @PathVariable Long groupId,
            @RequestBody SecurityGroupDto securityGroupDto
    ) {
        securityGroupDto.setId(groupId);
        return ResponseEntity.ok(securityGroupService.updateSecurityGroup(securityGroupDto));
    }

    @Operation(summary = "Security Group remove API", description = "Security Group을 삭제하기 위한 API")
    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteSecurityGroup(@PathVariable Long groupId) {
        securityGroupService.deleteSecurityGroup(groupId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "All Security Group get API", description = "모든 Security Group을 조회하기 위한 API")
    @GetMapping
    public ResponseEntity<List<SecurityGroupDto>> getAllSecurityGroups() {
        return ResponseEntity.ok(securityGroupService.getAllSecurityGroups());
    }

    @Operation(summary = "Security Group Member get API", description = "Security Group의 Member를 조회하기 위한 API")
    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<GroupMemberDto>> getAllGroupMembers(@PathVariable Long groupId) {
        return ResponseEntity.ok(securityGroupService.getAllGroupMembers(groupId));
    }

    @Operation(summary = "Security Group Member add API", description = "Security Group의 Member를 추가하기 위한 API")
    @PostMapping("/{groupId}/members")
    public ResponseEntity<GroupMemberDto> addGroupMember(
            @PathVariable Long groupId,
            @RequestBody GroupMemberDto groupMemberDto
    ) {
        groupMemberDto.setGroupId(groupId);
        return ResponseEntity.ok(securityGroupService.addGroupMember(groupMemberDto));
    }

    @Operation(summary = "Security Group Member delete API", description = "Security Group의 Member를 삭제하기 위한 API")
    @DeleteMapping("/{groupId}/members/{employeeId}")
    public ResponseEntity<Void> deleteGroupMember(
            @PathVariable Long groupId,
            @PathVariable Long employeeId
    ) {
        GroupMemberDto groupMemberDto = new GroupMemberDto();
        groupMemberDto.setGroupId(groupId);
        groupMemberDto.setEmployeeId(employeeId);
        securityGroupService.deleteGroupMember(groupMemberDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Security Group Area get API", description = "Security Group에 접근 가능 구역을 조회하기 위한 API")
    @GetMapping("/{groupId}/areas")
    public ResponseEntity<List<GroupAreaDto>> getAllGroupAreas(@PathVariable Long groupId) {
        return ResponseEntity.ok(securityGroupService.getAllGroupAreas(groupId));
    }

    @Operation(summary = "Security Group Area add API", description = "Security Group에 접근 가능 구역을 추가하기 위한 API")
    @PostMapping("/{groupId}/areas")
    public ResponseEntity<GroupAreaDto> addGroupArea(
            @PathVariable Long groupId,
            @RequestBody GroupAreaDto groupAreaDto
    ) {
        groupAreaDto.setGroupId(groupId);
        return ResponseEntity.ok(securityGroupService.addGroupArea(groupAreaDto));
    }

    @Operation(summary = "Security Group Area delete API", description = "Security Group에 접근 가능 구역을 삭제하기 위한 API")
    @DeleteMapping("/{groupId}/areas/{areaId}")
    public ResponseEntity<Void> deleteGroupArea(
            @PathVariable Long groupId,
            @PathVariable Long areaId
    ) {
        GroupAreaDto groupAreaDto = new GroupAreaDto();
        groupAreaDto.setGroupId(groupId);
        groupAreaDto.setAreaId(areaId);
        securityGroupService.deleteGroupArea(groupAreaDto);
        return ResponseEntity.noContent().build();
    }
}