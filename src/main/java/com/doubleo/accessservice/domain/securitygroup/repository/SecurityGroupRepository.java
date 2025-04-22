package com.doubleo.accessservice.domain.securitygroup.repository;


import com.doubleo.accessservice.domain.securitygroup.domain.SecurityGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityGroupRepository extends JpaRepository<SecurityGroup, Long> {
    SecurityGroup findByGroupId(Long groupId);

    void deleteSecurityGroup(SecurityGroup securityGroup);

    void deleteSecurityGroupByGroupName(String groupName);
}
