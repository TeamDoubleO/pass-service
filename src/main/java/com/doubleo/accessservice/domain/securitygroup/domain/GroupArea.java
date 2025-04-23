package com.doubleo.accessservice.domain.securitygroup.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "security_group_area",
        uniqueConstraints = @UniqueConstraint(columnNames = {"group_id", "area_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "group_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private SecurityGroup securityGroup;

    @Column(name = "area_id", nullable = false)
    private Long areaId;

    @Builder(access = AccessLevel.PRIVATE)
    private GroupArea(SecurityGroup securityGroup, Long areaId) {
        this.securityGroup = securityGroup;
        this.areaId = areaId;
    }

    public static GroupArea createGroupArea(SecurityGroup securityGroup, Long areaId) {
        return GroupArea.builder().securityGroup(securityGroup).areaId(areaId).build();
    }
}
