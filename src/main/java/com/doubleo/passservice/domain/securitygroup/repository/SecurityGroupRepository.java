package com.doubleo.passservice.domain.securitygroup.repository;

import com.doubleo.passservice.domain.securitygroup.domain.SecurityGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityGroupRepository extends JpaRepository<SecurityGroup, Long> {
    boolean existsByGroupName(String groupName);
}
