package com.doubleo.passservice.domain.stats.repository;

import com.doubleo.passservice.domain.stats.domain.EntryStatsDaily;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryStatsDailyRepository extends JpaRepository<EntryStatsDaily, Long> {}
