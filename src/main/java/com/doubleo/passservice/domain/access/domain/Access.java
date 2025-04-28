package com.doubleo.passservice.domain.access.domain;

import com.doubleo.passservice.domain.common.model.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
public class Access extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "access_id")
    private Long id;
}
