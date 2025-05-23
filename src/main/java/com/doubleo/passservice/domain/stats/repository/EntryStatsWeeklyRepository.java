package com.doubleo.passservice.domain.stats.repository;

import com.doubleo.passservice.domain.stats.domain.EntryStatsWeekly;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryStatsWeeklyRepository extends JpaRepository<EntryStatsWeekly, Long> {}
