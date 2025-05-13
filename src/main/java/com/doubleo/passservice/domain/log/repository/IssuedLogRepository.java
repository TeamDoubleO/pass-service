package com.doubleo.passservice.domain.log.repository;

import com.doubleo.passservice.domain.log.domain.IssuedLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssuedLogRepository extends JpaRepository<IssuedLog, Long> {
    List<IssuedLog> findAllByTenantId(String tenantId);
}
