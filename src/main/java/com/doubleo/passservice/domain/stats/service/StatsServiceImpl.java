package com.doubleo.passservice.domain.stats.service;

import com.doubleo.passservice.domain.stats.dto.response.DailyStatsListInfoResponse;
import com.doubleo.passservice.domain.stats.dto.response.WeeklyStatsListResponse;
import com.doubleo.passservice.domain.stats.repository.EntryStatsDailyRepository;
import com.doubleo.passservice.domain.stats.repository.EntryStatsWeeklyRepository;
import com.doubleo.passservice.global.util.TenantValidator;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final EntryStatsDailyRepository entryStatsDailyRepository;
    private final EntryStatsWeeklyRepository entryStatsWeeklyRepository;
    private final TenantValidator tenantValidator;

    @Override
    public List<DailyStatsListInfoResponse> getDailyPeriodStatsList() {

        String tenantId = tenantValidator.getTenantId();

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(15);

        return entryStatsDailyRepository.findDailyEnteredSumByDate(tenantId, today, startDate);
    }

    @Override
    public List<WeeklyStatsListResponse> getLastWeeksStats() {
        String tenantId = tenantValidator.getTenantId();
        LocalDate thisMonday = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate endDate = thisMonday.minusDays(1);
        LocalDate startDate = thisMonday.minusWeeks(5);

        return entryStatsWeeklyRepository.findLastWeeks(tenantId, startDate, endDate).stream()
                .map(
                        weekly ->
                                WeeklyStatsListResponse.of(
                                        weekly.getStartDate(),
                                        weekly.getEndDate(),
                                        weekly.getEntered()))
                .toList();
    }
}
