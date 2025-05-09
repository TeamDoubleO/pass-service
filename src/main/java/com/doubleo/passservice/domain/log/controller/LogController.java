package com.doubleo.passservice.domain.log.controller;

import com.doubleo.passservice.domain.log.dto.response.EnterLogResponse;
import com.doubleo.passservice.domain.log.dto.response.IssuedLogResponse;
import com.doubleo.passservice.domain.log.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
    public List<IssuedLogResponse> IssuedLogListGet(@RequestHeader("X-Admin-Id") Long adminId) {
        return logService.getAllIssuedLog();
    }

    @Operation(summary = "All issued log get API", description = "모든 출입 로그 조회 API")
    @GetMapping("/enter")
    public List<EnterLogResponse> EnterLogListGet(@RequestHeader("X-Admin-Id") Long adminId) {
        return logService.getAllEnterLog();
    }
}
