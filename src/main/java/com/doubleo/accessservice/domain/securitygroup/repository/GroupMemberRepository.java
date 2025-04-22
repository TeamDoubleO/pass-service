package com.doubleo.accessservice.domain.securitygroup.repository;

import com.doubleo.accessservice.domain.securitygroup.domain.GroupMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    Iterable<GroupMember> findByGroupId(Long groupId);

    List<GroupMember> findAllByGroupId(Long groupId);

    void deleteByGroupIdandEmployeeId(Long groupId, Long employeeId);
}
