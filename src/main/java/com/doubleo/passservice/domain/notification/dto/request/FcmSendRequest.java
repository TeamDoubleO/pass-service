package com.doubleo.passservice.domain.notification.dto.request;

public record FcmSendRequest(String token, String title, String content) {}
