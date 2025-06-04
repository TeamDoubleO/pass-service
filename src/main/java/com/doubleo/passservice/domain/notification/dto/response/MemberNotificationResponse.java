package com.doubleo.passservice.domain.notification.dto.response;

import com.doubleo.passservice.domain.notification.domain.MemberNotification;

public record MemberNotificationResponse(String title, String content) {
    public static MemberNotificationResponse from(MemberNotification notification) {
        return new MemberNotificationResponse(notification.getTitle(), notification.getContent());
    }
}
