package com.doubleo.passservice.domain.log.repository;

import com.doubleo.passservice.domain.log.domain.BuildingEnterLog;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BuildingEnterLogRepository extends JpaRepository<BuildingEnterLog, Long> {
    @Query(
            """
    SELECT COUNT(*) FROM BuildingEnterLog l
    WHERE l.direction = 'IN'
    AND l.createdDt >= :start AND l.createdDt < :end
""")
    int countInLogsAtHour(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    List<BuildingEnterLog> findAllByTenantIdAndCreatedDtBetween(
            String tenantId, LocalDateTime start, LocalDateTime end);
}
