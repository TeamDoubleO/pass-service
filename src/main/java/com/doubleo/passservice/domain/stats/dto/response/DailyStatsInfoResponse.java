package com.doubleo.passservice.domain.stats.dto.response;

import java.time.LocalDate;

public record DailyStatsInfoResponse(LocalDate date, Long total) {
    public static DailyStatsInfoResponse of(LocalDate date, Long total) {
        return new DailyStatsInfoResponse(date, total);
    }

    public DailyStatsInfoResponse(LocalDate date, Long total) {
        this.date = date;
        this.total = total;
    }
}
