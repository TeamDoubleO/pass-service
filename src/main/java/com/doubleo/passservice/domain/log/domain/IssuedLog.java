package com.doubleo.passservice.domain.log.domain;

import com.doubleo.passservice.domain.common.model.BaseEntity;
import com.doubleo.passservice.domain.pass.enums.VisitCategory;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "issued_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IssuedLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issued_log_id")
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Column(name = "member_contact", nullable = false)
    private String memberContact;

    @Column(name = "pass_id", nullable = false)
    private Long passId;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

    @Column(name = "visit_category")
    @Enumerated(EnumType.STRING)
    private VisitCategory visitCategory;
}
