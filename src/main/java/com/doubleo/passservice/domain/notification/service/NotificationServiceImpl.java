package com.doubleo.passservice.domain.notification.service;

import com.doubleo.passservice.domain.notification.dto.response.MemberNotificationResponse;
import com.doubleo.passservice.domain.notification.repository.MemberNotificationRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final MemberNotificationRepository memberNotificationRepository;

    @Override
    public List<MemberNotificationResponse> getAllMemberNotifications(Long memberId, Long days) {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(days);

        return memberNotificationRepository
                .findAllByMemberIdAndCreatedDtAfter(memberId, sevenDaysAgo)
                .stream()
                .map(MemberNotificationResponse::from)
                .toList();
    }

    @Override
    public void deleteAllMemberNotifications(Long memberId) {
        memberNotificationRepository.deleteAllByMemberId(memberId);
    }
}
