package com.doubleo.accessservice.domain.access.domain;

import com.doubleo.accessservice.domain.common.model.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
public class Access extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "access_id")
    private Long id;
}
