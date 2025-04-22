package com.doubleo.accessservice.domain.securitygroup.repository;

import com.doubleo.accessservice.domain.securitygroup.domain.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    Iterable<GroupMember> findByGroupId(Long groupId);
    List<GroupMember> findAllByGroupId(Long groupId);
    void deleteByGroupIdandEmployeeId(Long groupId, Long employeeId);
}
