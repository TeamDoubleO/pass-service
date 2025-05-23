package com.doubleo.passservice.domain.stats.service;

import com.doubleo.passservice.domain.stats.dto.response.DailyStatInfoResponse;
import java.util.List;

public interface StatsService {

    List<DailyStatInfoResponse> getDailyPeriodStatList();
}
