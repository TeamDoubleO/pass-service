package com.doubleo.passservice.domain.pass.controller;

import com.doubleo.passservice.domain.pass.dto.request.PassCreateRequest;
import com.doubleo.passservice.domain.pass.dto.request.PassDeleteRequest;
import com.doubleo.passservice.domain.pass.dto.request.UpdatePassStatusRequest;
import com.doubleo.passservice.domain.pass.dto.response.MemberPassInfoResponse;
import com.doubleo.passservice.domain.pass.dto.response.PassCreateResponse;
import com.doubleo.passservice.domain.pass.dto.response.PendingPassResponse;
import com.doubleo.passservice.domain.pass.service.PassService;
import com.doubleo.passservice.global.exception.CommonException;
import com.doubleo.passservice.global.exception.errorcode.PassErrorCode;
import com.doubleo.passservice.global.util.TenantValidator;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/passes")
@RequiredArgsConstructor
public class PassController {

    private final PassService passService;
    private final TenantValidator tenantValidator;

    @Operation(summary = "All Member Pass get API", description = "모든 사용자의 Pass들을 조회하는 API")
    @GetMapping
    public List<MemberPassInfoResponse> MemberPassInfoListGet(
            @RequestHeader("X-Member-Id") Long memberId) {
        return passService.getAllMemberPassInfo(memberId);
    }

    @Operation(summary = "Pass apply API", description = "Pass 발급 신청 API")
    @PostMapping
    public PassCreateResponse MemberPassCreate(
            @RequestHeader("X-Member-Id") Long memberId,
            @RequestHeader("X-Hospital-Id") Long hospitalId,
            @RequestHeader("X-Tenant-Id") String tenantId,
            @RequestBody PassCreateRequest request) {
        switch (request.visitCategory()) {
            case PATIENT -> {
                return passService.createPatientPass(
                        memberId, hospitalId, tenantId, request.startAt());
            }
            case GUARDIAN -> {
                if (request.patientCode() != null) {
                    return passService.createGuardianPass(
                            memberId,
                            hospitalId,
                            tenantId,
                            request.patientCode(),
                            request.startAt());
                } else throw new CommonException(PassErrorCode.PATIENT_ID_REQUIRED_FOR_GUARDIAN);
            }
            default -> {
                throw new CommonException(PassErrorCode.VISIT_CATEGORY_REQUIRED_FOR_PASS);
            }
        }
    }

    @Operation(summary = "Pass delete API", description = "Pass 삭제 API")
    @DeleteMapping
    public ResponseEntity<Void> PassDelete(
            @RequestHeader("X-Member-Id") Long memberId, @RequestBody PassDeleteRequest request) {
        passService.deletePass(request.passId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "All pending pass get API", description = "모든 발급 대기중인 출입증 조회 API")
    @GetMapping("/pending")
    public Page<PendingPassResponse> PendingPassListGet(
            @RequestHeader("X-Admin-Id") Long adminId, Pageable pageable) {
        String tenantId = tenantValidator.getTenantId();
        return passService.getPendingPassList(tenantId, pageable);
    }

    @Operation(summary = "Accept Guardian application", description = "보호자 출입증 신청 승인 API")
    @PostMapping("/approve")
    public PassCreateResponse GuardianApplicationCreate(
            @RequestHeader("X-Admin-Id") Long adminId,
            @RequestBody UpdatePassStatusRequest request) {
        return passService.createGuardianAndUpdatePassStatus(
                request.passId(), request.issuanceStatus());
    }
}
