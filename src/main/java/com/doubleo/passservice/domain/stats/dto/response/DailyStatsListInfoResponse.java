package com.doubleo.passservice.domain.stats.dto.response;

import java.time.LocalDate;

public record DailyStatsListInfoResponse(LocalDate date, Long total) {
    public static DailyStatsListInfoResponse of(LocalDate date, Long total) {
        return new DailyStatsListInfoResponse(date, total);
    }

    public DailyStatsListInfoResponse(LocalDate date, Long total) {
        this.date = date;
        this.total = total;
    }
}
