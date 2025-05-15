package com.doubleo.passservice.domain.log.controller;

import com.doubleo.passservice.domain.log.dto.response.EnterLogResponse;
import com.doubleo.passservice.domain.log.dto.response.IssuedLogResponse;
import com.doubleo.passservice.domain.log.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pass-logs")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @Operation(summary = "All issued log get API", description = "모든 출입증 발급 로그 조회 API")
    @GetMapping("/issued")
    public Page<IssuedLogResponse> IssuedLogListGet(
            @RequestHeader("X-Admin-Id") Long adminId, Pageable pageable) {
        return logService.getAllIssuedLog(pageable);
    }

    @Operation(summary = "All issued log get API", description = "모든 출입 로그 조회 API")
    @GetMapping("/enter")
    public Page<EnterLogResponse> EnterLogListGet(
            @RequestHeader("X-Admin-Id") Long adminId, Pageable pageable) {
        return logService.getAllEnterLog(pageable);
    }
}
