package com.doubleo.passservice.domain.notification.dto.request;

import jakarta.validation.constraints.NotBlank;

public record FcmSendRequest(
        @NotBlank String token, @NotBlank String title, @NotBlank String content) {}
