package com.doubleo.passservice.domain.pass.domain;

import com.doubleo.passservice.domain.common.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "pass")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pass extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pass_id")
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "request_member")
    private String requestMember;

    @Column(name = "valid_time")
    private Date validTime;

    @Column(name = "request_member_category")
    private String requestMemberCategory;

    @Column(name = "patient_id")
    private Long patientId;
}
