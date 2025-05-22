package com.doubleo.passservice.domain.log.repository;

import com.doubleo.passservice.domain.log.domain.IssuedLog;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssuedLogRepository extends JpaRepository<IssuedLog, Long> {
    List<IssuedLog> findAllByTenantId(String tenantId);

    Page<IssuedLog> findAllByTenantId(String tenantId, Pageable pageable);

    Optional<IssuedLog> findByPassId(Long passId);
}
