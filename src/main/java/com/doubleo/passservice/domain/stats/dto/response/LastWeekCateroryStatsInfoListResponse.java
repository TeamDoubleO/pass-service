package com.doubleo.passservice.domain.stats.dto.response;

import java.time.LocalDate;

public record LastWeekCateroryStatsInfoListResponse(
        LocalDate date, String day, String category, long total) {}
