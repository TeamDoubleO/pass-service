package com.doubleo.passservice.domain.stats.service;

import com.doubleo.passservice.domain.stats.dto.response.*;
import java.util.List;

public interface StatsService {

    List<DailyStatsInfoListResponse> getDailyPeriodStatsList();

    List<WeeklyStatsInfoListResponse> getLastWeeksStatsList();

    List<MonthlyStatsInfoListResponse> getRecentMonthlyStatsList();

    List<LastWeekCategoryStatsInfoListResponse> getLastWeekCategoryStats();

    List<LastWeekBuildingStatsInfoListResponse> getLastWeekBuildingStats();
}
