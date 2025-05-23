package com.doubleo.passservice.domain.stats.service;

import com.doubleo.passservice.domain.stats.dto.response.DailyStatsInfoResponse;
import java.util.List;

public interface StatsService {

    List<DailyStatsInfoResponse> getDailyPeriodStatsList();
}
