package com.doubleo.accessservice.domain.securitygroup.repository;

import com.doubleo.accessservice.domain.securitygroup.domain.GroupArea;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupAreaRepository extends JpaRepository<GroupArea, Long> {
    Iterable<GroupArea> findByGroupId(Long groupId);

    List<GroupArea> findAllByGroupId(Long groupId);

    void deleteByGroupIdandGroupAreaId(Long groupId, Long groupAreaId);
}
