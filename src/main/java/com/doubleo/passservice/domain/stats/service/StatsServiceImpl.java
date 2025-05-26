package com.doubleo.passservice.domain.stats.service;

import com.doubleo.passservice.domain.stats.domain.EntryStatsDaily;
import com.doubleo.passservice.domain.stats.dto.response.*;
import com.doubleo.passservice.domain.stats.repository.EntryStatsDailyRepository;
import com.doubleo.passservice.domain.stats.repository.EntryStatsMonthlyRepository;
import com.doubleo.passservice.domain.stats.repository.EntryStatsWeeklyRepository;
import com.doubleo.passservice.global.util.TenantValidator;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final EntryStatsDailyRepository entryStatsDailyRepository;
    private final EntryStatsWeeklyRepository entryStatsWeeklyRepository;
    private final EntryStatsMonthlyRepository entryStatsMonthlyRepository;
    private final TenantValidator tenantValidator;

    @Override
    public List<DailyStatsInfoListResponse> getDailyPeriodStatsList() {

        String tenantId = tenantValidator.getTenantId();

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(15);

        return entryStatsDailyRepository.findDailyEnteredSumByDate(tenantId, today, startDate);
    }

    @Override
    public List<WeeklyStatsInfoListResponse> getLastWeeksStatsList() {
        String tenantId = tenantValidator.getTenantId();
        LocalDate thisMonday = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate endDate = thisMonday.minusDays(1);
        LocalDate startDate = thisMonday.minusWeeks(5);

        return entryStatsWeeklyRepository.findLastWeeks(tenantId, startDate, endDate).stream()
                .map(
                        weekly ->
                                WeeklyStatsInfoListResponse.of(
                                        weekly.getStartDate(),
                                        weekly.getEndDate(),
                                        weekly.getEntered()))
                .toList();
    }

    public List<MonthlyStatsInfoListResponse> getRecentMonthlyStatsList() {
        LocalDate now = LocalDate.now();
        return entryStatsMonthlyRepository
                .findUpToPreviousMonth(
                        tenantValidator.getTenantId(), now.getYear(), now.getMonthValue())
                .stream()
                .limit(12)
                .map(
                        e ->
                                MonthlyStatsInfoListResponse.of(
                                        e.getYear(), e.getMonth(), e.getEntered()))
                .toList();
    }

    public List<LastWeekCategoryStatsInfoListResponse> getLastWeekCategoryStats() {
        String tenantId = tenantValidator.getTenantId();
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(7);
        LocalDate endDate = today;

        List<EntryStatsDaily> stats =
                entryStatsDailyRepository.findLastWeekStats(tenantId, startDate, endDate);

        return stats.stream()
                .collect(
                        Collectors.groupingBy(
                                EntryStatsDaily::getDate,
                                Collectors.groupingBy(
                                        EntryStatsDaily::getVisitCategory,
                                        Collectors.summingLong(EntryStatsDaily::getEntered))))
                .entrySet()
                .stream()
                .flatMap(
                        entry -> {
                            LocalDate date = entry.getKey();
                            String day =
                                    date.getDayOfWeek()
                                            .getDisplayName(TextStyle.SHORT, Locale.KOREAN);
                            return entry.getValue().entrySet().stream()
                                    .map(
                                            catEntry ->
                                                    new LastWeekCategoryStatsInfoListResponse(
                                                            date,
                                                            day,
                                                            catEntry.getKey().name(),
                                                            catEntry.getValue()));
                        })
                .collect(Collectors.toList());
    }

    public List<LastWeekBuildingStatsInfoListResponse> getLastWeekBuildingStats() {
        String tenantId = tenantValidator.getTenantId();
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(7);
        LocalDate endDate = today;

        List<EntryStatsDaily> stats =
                entryStatsDailyRepository.findLastWeekStats(tenantId, startDate, endDate);

        return stats.stream()
                .collect(
                        Collectors.groupingBy(
                                EntryStatsDaily::getDate,
                                Collectors.groupingBy(
                                        EntryStatsDaily::getBuildingName,
                                        Collectors.summingLong(EntryStatsDaily::getEntered))))
                .entrySet()
                .stream()
                .flatMap(
                        dateEntry -> {
                            LocalDate date = dateEntry.getKey();
                            String day =
                                    date.getDayOfWeek()
                                            .getDisplayName(TextStyle.SHORT, Locale.KOREAN);

                            return dateEntry.getValue().entrySet().stream()
                                    .map(
                                            buildingEntry ->
                                                    new LastWeekBuildingStatsInfoListResponse(
                                                            date,
                                                            day,
                                                            buildingEntry.getKey(),
                                                            buildingEntry.getValue()));
                        })
                .collect(Collectors.toList());
    }
}
