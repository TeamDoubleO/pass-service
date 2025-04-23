package com.doubleo.accessservice.domain.securitygroup.repository;

import com.doubleo.accessservice.domain.securitygroup.domain.GroupMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    List<GroupMember> findAllBySecurityGroup_Id(Long groupId);

    void deleteBySecurityGroup_IdAndEmployeeId(Long groupId, Long employeeId);
}
