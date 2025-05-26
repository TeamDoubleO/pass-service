package com.doubleo.passservice.domain.stats.controller;

import com.doubleo.passservice.domain.stats.dto.response.DailyStatsInfoListResponse;
import com.doubleo.passservice.domain.stats.dto.response.LastWeekCategoryStatsInfoListResponse;
import com.doubleo.passservice.domain.stats.dto.response.MonthlyStatsInfoListResponse;
import com.doubleo.passservice.domain.stats.dto.response.WeeklyStatsInfoListResponse;
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
    public List<DailyStatsInfoListResponse> dailyPeriodStatsListGet() {
        return statsService.getDailyPeriodStatsList();
    }

    @GetMapping("/period/weekly")
    public List<WeeklyStatsInfoListResponse> weeklyPeriodStatsListGet() {
        return statsService.getLastWeeksStatsList();
    }

    @GetMapping("/period/monthly")
    public List<MonthlyStatsInfoListResponse> monthlyPeriodStatsListGet() {
        return statsService.getRecentMonthlyStatsList();
    }

    @GetMapping("/weekly-category-summary")
    public List<LastWeekCategoryStatsInfoListResponse> lastWeekCategoryStatsListGet() {
        return statsService.getLastWeekCategoryStats();
    }
}
