package com.doubleo.accessservice.domain.securitygroup.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "security_group_member",
        uniqueConstraints = @UniqueConstraint(columnNames = {"group_id", "employee_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "group_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private SecurityGroup securityGroup;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Builder(access = AccessLevel.PRIVATE)
    private GroupMember(SecurityGroup securityGroup, Long employeeId) {
        this.securityGroup = securityGroup;
        this.employeeId = employeeId;
    }

    public static GroupMember createGroupMember(SecurityGroup securityGroup, Long employeeId) {
        return GroupMember.builder().securityGroup(securityGroup).employeeId(employeeId).build();
    }
}
