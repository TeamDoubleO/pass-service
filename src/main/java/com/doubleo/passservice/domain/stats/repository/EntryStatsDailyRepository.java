package com.doubleo.passservice.domain.stats.repository;

import com.doubleo.passservice.domain.stats.domain.EntryStatsDaily;
import com.doubleo.passservice.domain.stats.dto.response.DailyStatsInfoResponse;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EntryStatsDailyRepository extends JpaRepository<EntryStatsDaily, Long> {
    @Query(
            """
    SELECT new com.doubleo.passservice.domain.stats.dto.response.DailyStatsInfoResponse(e.date, SUM(e.entered))
    FROM EntryStatsDaily e
    WHERE e.tenantId = :tenantId
      AND e.date < :today
      AND e.date >= :startDate
    GROUP BY e.date
    ORDER BY e.date DESC
""")
    List<DailyStatsInfoResponse> findDailyEnteredSumByDate(
            String tenantId, LocalDate today, LocalDate startDate);
}
