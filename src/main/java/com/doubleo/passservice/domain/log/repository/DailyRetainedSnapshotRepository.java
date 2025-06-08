package com.doubleo.passservice.domain.log.repository;

import com.doubleo.passservice.domain.log.domain.DailyRetainedSnapshot;
import com.doubleo.passservice.domain.pass.enums.VisitCategory;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyRetainedSnapshotRepository
        extends JpaRepository<DailyRetainedSnapshot, Long> {
    Optional<DailyRetainedSnapshot> findBySnapshotDateAndTenantIdAndVisitCategory(
            LocalDate snapshotDate, String tenantId, VisitCategory visitCategory);
}
