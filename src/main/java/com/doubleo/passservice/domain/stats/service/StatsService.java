package com.doubleo.passservice.domain.stats.service;

import com.doubleo.passservice.domain.stats.dto.response.DailyStatsInfoListResponse;
import com.doubleo.passservice.domain.stats.dto.response.WeeklyStatsInfoListResponse;
import java.util.List;

public interface StatsService {

    List<DailyStatsInfoListResponse> getDailyPeriodStatsList();

    List<WeeklyStatsInfoListResponse> getLastWeeksStatsList();
}
