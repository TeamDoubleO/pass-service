package com.doubleo.accessservice.domain.securitygroup.repository;

import com.doubleo.accessservice.domain.securitygroup.domain.GroupArea;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupAreaRepository extends JpaRepository<GroupArea, Long> {
    List<GroupArea> findAllBySecurityGroup_Id(Long groupId);

    void deleteBySecurityGroup_IdAndAreaId(Long groupId, Long groupAreaId);
}
