package com.doubleo.passservice.domain.stats.scheduler;

import com.doubleo.passservice.domain.stats.service.StatsBatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EntryStatsScheduler {

    private final StatsBatchService statsBatchService;

    @Scheduled(cron = "0 10 0 * * *")
    public void runDailyStatsUpdate() {
        statsBatchService.updateDailyStats();
    }

    @Scheduled(cron = "0 20 0 * * MON")
    public void runWeeklyStatsUpdate() {
        statsBatchService.updateWeeklyStats();
    }

    @Scheduled(cron = "0 30 0 1 * *")
    public void runMonthlyStatsUpdate() {
        statsBatchService.updateMonthlyStats();
    }
}
