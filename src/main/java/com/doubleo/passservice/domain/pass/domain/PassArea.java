package com.doubleo.passservice.domain.pass.domain;

import com.doubleo.passservice.domain.common.model.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "pass_area")
public class PassArea extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pass_area_id")
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @JoinColumn(name = "pass_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Pass pass;

    @Column(name = "area_id", nullable = false)
    private Long areaId;
}
