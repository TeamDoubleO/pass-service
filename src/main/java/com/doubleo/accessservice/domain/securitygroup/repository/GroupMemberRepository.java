package com.doubleo.accessservice.domain.securitygroup.repository;

import com.doubleo.accessservice.domain.securitygroup.domain.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
}
