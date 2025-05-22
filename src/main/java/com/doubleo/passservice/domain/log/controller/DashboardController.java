package com.doubleo.passservice.domain.log.controller;

import com.doubleo.passservice.domain.log.dto.response.HourlyEntryResponse;
import com.doubleo.passservice.domain.log.service.DashboardService;
import com.doubleo.passservice.global.util.TenantValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pass-logs")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final TenantValidator tenantValidator;

    @GetMapping("/hourly")
    public List<HourlyEntryResponse> hourlyEntryListGet() {
        return dashboardService.getHourlyEntryList();
    }
}
