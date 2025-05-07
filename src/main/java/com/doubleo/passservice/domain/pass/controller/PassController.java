package com.doubleo.passservice.domain.pass.controller;

import com.doubleo.passservice.domain.pass.dto.response.MemberPassInfoResponse;
import com.doubleo.passservice.domain.pass.service.PassService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/passes")
@RequiredArgsConstructor
public class PassController {

    private final PassService passService;

    @Operation(summary = "All Member Pass get API", description = "모든 사용자의 Pass들을 조회하는 API")
    @GetMapping
    public ResponseEntity<List<MemberPassInfoResponse>> getAllMemberPassInfo(
            @RequestHeader("X-Member-Id") Long memberId) {
        return ResponseEntity.ok(passService.getAllMemberPassInfo(memberId));
    }
}
