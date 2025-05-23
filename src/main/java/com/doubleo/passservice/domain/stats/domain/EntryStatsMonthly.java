package com.doubleo.passservice.domain.stats.domain;

import com.doubleo.passservice.domain.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "entry_stats_monthly",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"tenant_id", "year", "month"})})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EntryStatsMonthly extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entry_stats_monthly_year", nullable = false)
    private int year;

    @Column(name = "entry_stats_monthly_month", nullable = false)
    private int month;

    @Column(name = "entry_stats_monthly_entered", nullable = false)
    private Long entered;
}
