package com.doubleo.accessservice.domain.securitygroup.repository;

import com.doubleo.accessservice.domain.securitygroup.domain.GroupArea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupAreaRepository extends JpaRepository<GroupArea, Long> {
    Iterable<GroupArea> findByGroupId(Long groupId);
    List<GroupArea> findAllByGroupId(Long groupId);
    void deleteByGroupIdandGroupAreaId(Long groupId, Long groupAreaId);
}
