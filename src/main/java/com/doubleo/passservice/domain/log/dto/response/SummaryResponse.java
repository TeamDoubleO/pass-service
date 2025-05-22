package com.doubleo.passservice.domain.log.dto.response;

public record SummaryResponse(String category, int entered, int exited, int remaining) {}
