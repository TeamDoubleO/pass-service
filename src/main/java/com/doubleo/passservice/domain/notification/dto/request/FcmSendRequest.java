package com.doubleo.passservice.domain.notification.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FcmSendRequest(
        @NotNull Long memberId,
        @Nullable String token,
        @NotBlank String title,
        @NotBlank String content) {}
