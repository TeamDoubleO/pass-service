package com.doubleo.passservice.domain.pass.domain;

import com.doubleo.passservice.domain.common.model.BaseEntity;
import com.doubleo.passservice.domain.pass.enums.VisitCategory;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pass")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pass extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pass_id")
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "hospital_id", nullable = false)
    private Long hospitalId;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "visit_category")
    @Enumerated(EnumType.STRING)
    private VisitCategory visitCategory;

    @Builder(access = AccessLevel.PRIVATE)
    private Pass(
            String tenantId,
            Long memberId,
            Long hospitalId,
            LocalDateTime startAt,
            LocalDateTime expiredAt,
            Long patientId,
            VisitCategory visitCategory) {
        this.tenantId = tenantId;
        this.memberId = memberId;
        this.hospitalId = hospitalId;
        this.startAt = startAt;
        this.expiredAt = expiredAt;
        this.patientId = patientId;
        this.visitCategory = visitCategory;
    }

    public static Pass createPass(
            String tenantId,
            Long memberId,
            Long hospitalId,
            LocalDateTime startAt,
            LocalDateTime expiredAt) {
        return Pass.builder()
                .tenantId(tenantId)
                .memberId(memberId)
                .hospitalId(hospitalId)
                .startAt(startAt)
                .expiredAt(expiredAt)
                .build();
    }
}
