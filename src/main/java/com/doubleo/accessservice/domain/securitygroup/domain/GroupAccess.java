package com.doubleo.accessservice.domain.securitygroup.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "security_group_access")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupAccess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "group_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private SecurityGroup securityGroup;

    @Column(name = "area_id", nullable = false)
    private Long areaId;

    @Builder(access = AccessLevel.PRIVATE)
    private GroupAccess(SecurityGroup securityGroup, Long areaId) {
        this.securityGroup = securityGroup;
        this.areaId = areaId;
    }

    public GroupAccess createGroupAccess(SecurityGroup securityGroup, Long areaId) {
        return GroupAccess.builder().securityGroup(securityGroup).areaId(areaId).build();
    }
}
