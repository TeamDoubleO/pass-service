package com.doubleo.accessservice.domain.securitygroup.repository;

import com.doubleo.accessservice.domain.securitygroup.domain.GroupAccess;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupAccessRepository extends JpaRepository<GroupAccess, Long> {
}
