package com.doubleo.passservice.domain.securitygroup.repository;

import com.doubleo.passservice.domain.securitygroup.domain.GroupArea;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupAreaRepository extends JpaRepository<GroupArea, Long> {
    List<GroupArea> findAllBySecurityGroup_Id(Long groupId);

    void deleteBySecurityGroup_IdAndAreaId(Long groupId, Long groupAreaId);

    boolean existsBySecurityGroup_IdAndAreaId(Long groupId, @NotNull Long aLong);
}
