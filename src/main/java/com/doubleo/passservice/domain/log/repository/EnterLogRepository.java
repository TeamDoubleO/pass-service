package com.doubleo.passservice.domain.log.repository;

import com.doubleo.passservice.domain.log.domain.EnterLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnterLogRepository extends JpaRepository<EnterLog, Long> {}
