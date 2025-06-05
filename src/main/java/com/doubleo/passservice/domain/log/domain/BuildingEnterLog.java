package com.doubleo.passservice.domain.log.domain;

import com.doubleo.passservice.domain.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "building_enter_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BuildingEnterLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "building_enter_log_id")
    private Long id;

    @Column(name = "building_id", nullable = false)
    private Long buildingId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Column(name = "pass_id", nullable = false)
    private Long passId;

    @Enumerated(EnumType.STRING)
    @Column(name = "direction", nullable = false, length = 10)
    private Direction direction;

    public static BuildingEnterLog createBuildingEnterLog(
            Long buildingId,
            Long memberId,
            String memberName,
            Long passId,
            Direction direction,
            String tenantId) {
        BuildingEnterLog log = new BuildingEnterLog();
        log.buildingId = buildingId;
        log.memberId = memberId;
        log.memberName = memberName;
        log.passId = passId;
        log.direction = direction;
        log.tenantId = tenantId;
        return log;
    }
}
