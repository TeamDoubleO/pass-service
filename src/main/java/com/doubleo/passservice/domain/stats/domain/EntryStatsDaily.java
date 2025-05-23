package com.doubleo.passservice.domain.stats.domain;

import com.doubleo.passservice.domain.common.model.BaseEntity;
import com.doubleo.passservice.domain.pass.enums.VisitCategory;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "entry_stats_daily",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"tenant_id", "date", "building_id", "visit_category"})
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EntryStatsDaily extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entry_stats_daily_date", nullable = false)
    private LocalDate date;

    @Column(name = "entry_stats_daily_building_id", nullable = false)
    private String buildingId;

    @Column(name = "entry_stats_daily_visit_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private VisitCategory visitCategory;

    @Column(name = "entry_stats_daily_entered", nullable = false)
    private Long entered;

    @Column(name = "entry_stats_daily_exited", nullable = false)
    private Long exited;
}
