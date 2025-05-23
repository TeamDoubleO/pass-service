package com.doubleo.passservice.domain.stats.repository;

import com.doubleo.passservice.domain.stats.domain.EntryStatsMonthly;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryStatsMonthlyRepository extends JpaRepository<EntryStatsMonthly, Long> {}
