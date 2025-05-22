package com.doubleo.passservice.domain.log.service;

import com.doubleo.passservice.domain.log.dto.response.HourlyEntryResponse;
import java.util.List;

public interface DashboardService {
    List<HourlyEntryResponse> getHourlyEntryList();
}
