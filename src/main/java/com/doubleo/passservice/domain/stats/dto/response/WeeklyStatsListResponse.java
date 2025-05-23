package com.doubleo.passservice.domain.stats.dto.response;

import java.time.LocalDate;

public record WeeklyStatsListResponse(LocalDate startDate, LocalDate endDate, Long entered) {
    public static WeeklyStatsListResponse of(LocalDate start, LocalDate end, Long total) {
        return new WeeklyStatsListResponse(start, end, total);
    }
}
