package com.doubleo.passservice.domain.stats.dto.response;

import java.time.LocalDate;

public record DailyStatInfoResponse(LocalDate date, Long total) {
    public static DailyStatInfoResponse of(LocalDate date, Long total) {
        return new DailyStatInfoResponse(date, total);
    }
}
