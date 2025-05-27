package com.doubleo.passservice.domain.pass.dto.response;

import java.time.LocalDate;

public record PassCountInfoResponse(
        LocalDate date, Long areaId, String areaCode, String areaName, long passCount) {}
