package com.doubleo.passservice.domain.notification.repository;

import com.doubleo.passservice.domain.notification.domain.MemberNotification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberNotificationRepository extends JpaRepository<MemberNotification, Long> {
    List<MemberNotification> findAllByMemberId(Long memberId);

    void deleteAllByMemberId(Long memberId);
}
