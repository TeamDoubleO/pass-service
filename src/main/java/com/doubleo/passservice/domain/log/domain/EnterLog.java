package com.doubleo.passservice.domain.log.domain;

import com.doubleo.passservice.domain.common.model.BaseTimeEntity;
import com.doubleo.passservice.domain.pass.domain.Pass;
import jakarta.persistence.*;

@Entity
@Table(name = "enter_log")
public class EnterLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enter_log_id")
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @JoinColumn(name = "pass_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Pass pass;
}
