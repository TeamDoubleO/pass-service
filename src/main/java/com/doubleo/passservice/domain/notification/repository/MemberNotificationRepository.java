package com.doubleo.passservice.domain.notification.repository;

import com.doubleo.passservice.domain.notification.domain.MemberNotification;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberNotificationRepository extends JpaRepository<MemberNotification, Long> {
    List<MemberNotification> findAllByMemberId(Long memberId);

    void deleteAllByMemberId(Long memberId);

    List<MemberNotification> findAllByMemberIdAndCreatedDtAfter(
            Long memberId, LocalDateTime createdDtAfter);
}
