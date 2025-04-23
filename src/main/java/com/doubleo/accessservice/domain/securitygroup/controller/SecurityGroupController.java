package com.doubleo.accessservice.domain.securitygroup.controller;

import com.doubleo.accessservice.domain.securitygroup.dto.request.GroupAreaRequest;
import com.doubleo.accessservice.domain.securitygroup.dto.request.GroupMemberRequest;
import com.doubleo.accessservice.domain.securitygroup.dto.request.SecurityGroupRequest;
import com.doubleo.accessservice.domain.securitygroup.dto.response.GroupAreaResponse;
import com.doubleo.accessservice.domain.securitygroup.dto.response.GroupMemberResponse;
import com.doubleo.accessservice.domain.securitygroup.dto.response.SecurityGroupResponse;
import com.doubleo.accessservice.domain.securitygroup.service.SecurityGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<SecurityGroupResponse> createSecurityGroup(
            @RequestBody SecurityGroupRequest request) {
        return ResponseEntity.ok(securityGroupService.createSecurityGroup(request));
    }

    @Operation(summary = "Security Group update API", description = "Security Group을 수정하기 위한 API")
    @PatchMapping("/{groupId}")
    public ResponseEntity<SecurityGroupResponse> updateSecurityGroup(
            @PathVariable Long groupId, @RequestBody SecurityGroupRequest request) {
        return ResponseEntity.ok(securityGroupService.updateSecurityGroup(groupId, request));
    }

    @Operation(summary = "Security Group remove API", description = "Security Group을 삭제하기 위한 API")
    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteSecurityGroup(@PathVariable Long groupId) {
        securityGroupService.deleteSecurityGroup(groupId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "All Security Group get API",
            description = "모든 Security Group을 조회하기 위한 API")
    @GetMapping
    public ResponseEntity<List<SecurityGroupResponse>> getAllSecurityGroups() {
        return ResponseEntity.ok(securityGroupService.getAllSecurityGroups());
    }

    @Operation(
            summary = "Security Group Member get API",
            description = "Security Group의 Member를 조회하기 위한 API")
    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<GroupMemberResponse>> getAllGroupMembers(
            @PathVariable Long groupId) {
        return ResponseEntity.ok(securityGroupService.getAllGroupMembers(groupId));
    }

    @Operation(
            summary = "Security Group Member add API",
            description = "Security Group의 Member를 추가하기 위한 API")
    @PostMapping("/{groupId}/members")
    public ResponseEntity<GroupMemberResponse> addGroupMember(
            @PathVariable Long groupId, @RequestBody GroupMemberRequest request) {
        return ResponseEntity.ok(securityGroupService.addGroupMember(groupId, request));
    }

    @Operation(
            summary = "Security Group Member delete API",
            description = "Security Group의 Member를 삭제하기 위한 API")
    @DeleteMapping("/{groupId}/members/{employeeId}")
    public ResponseEntity<Void> deleteGroupMember(
            @PathVariable Long groupId, @PathVariable GroupMemberRequest request) {
        securityGroupService.deleteGroupMember(groupId, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Security Group Area get API",
            description = "Security Group에 접근 가능 구역을 조회하기 위한 API")
    @GetMapping("/{groupId}/areas")
    public ResponseEntity<List<GroupAreaResponse>> getAllGroupAreas(@PathVariable Long groupId) {
        return ResponseEntity.ok(securityGroupService.getAllGroupAreas(groupId));
    }

    @Operation(
            summary = "Security Group Area add API",
            description = "Security Group에 접근 가능 구역을 추가하기 위한 API")
    @PostMapping("/{groupId}/areas")
    public ResponseEntity<GroupAreaResponse> addGroupArea(
            @PathVariable Long groupId, @RequestBody GroupAreaRequest request) {
        return ResponseEntity.ok(securityGroupService.addGroupArea(groupId, request));
    }

    @Operation(
            summary = "Security Group Area delete API",
            description = "Security Group에 접근 가능 구역을 삭제하기 위한 API")
    @DeleteMapping("/{groupId}/areas/{areaId}")
    public ResponseEntity<Void> deleteGroupArea(
            @PathVariable Long groupId, @PathVariable GroupAreaRequest request) {
        securityGroupService.deleteGroupArea(groupId, request);
        return ResponseEntity.noContent().build();
    }
}
