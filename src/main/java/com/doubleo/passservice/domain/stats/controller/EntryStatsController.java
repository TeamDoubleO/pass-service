package com.doubleo.passservice.domain.stats.controller;

import com.doubleo.passservice.domain.stats.dto.response.DailyStatsInfoResponse;
import com.doubleo.passservice.domain.stats.service.StatsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pass-logs")
@RequiredArgsConstructor
public class EntryStatsController {

    private final StatsService statsService;

    @GetMapping("/period/daily")
    public List<DailyStatsInfoResponse> dailyPeriodStatsListGet() {
        return statsService.getDailyPeriodStatsList();
    }
}
