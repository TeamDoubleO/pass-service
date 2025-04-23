package com.doubleo.accessservice.domain.securitygroup.domain;

import com.doubleo.accessservice.domain.common.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "security_group")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SecurityGroup extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @Column(name = "group_name", nullable = false, unique = true)
    private String groupName;

    @Column(name = "description")
    private String description;

    @Builder(access = AccessLevel.PRIVATE)
    private SecurityGroup(String groupName, String description) {
        this.groupName = groupName;
        this.description = description;
    }

    public void updateSecurityGroup(String groupName, String description) {
        this.groupName = groupName;
        this.description = description;
    }

    public static SecurityGroup createSecurityGroup(String groupName, String description) {
        return SecurityGroup.builder().groupName(groupName).description(description).build();
    }
}
