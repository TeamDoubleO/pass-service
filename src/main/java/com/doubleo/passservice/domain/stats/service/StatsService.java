package com.doubleo.passservice.domain.stats.service;

import com.doubleo.passservice.domain.stats.dto.response.DailyStatsListInfoResponse;
import com.doubleo.passservice.domain.stats.dto.response.WeeklyStatsListResponse;
import java.util.List;

public interface StatsService {

    List<DailyStatsListInfoResponse> getDailyPeriodStatsList();

    List<WeeklyStatsListResponse> getLastWeeksStats();
}
