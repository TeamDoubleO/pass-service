package com.doubleo.passservice.domain.pass.domain;

import com.doubleo.passservice.domain.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pass_area")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PassArea extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pass_area_id")
    private Long id;

    @JoinColumn(name = "pass_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Pass pass;

    @Column(name = "area_code", nullable = false)
    private String areaCode;
}
