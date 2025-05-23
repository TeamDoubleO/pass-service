package com.doubleo.passservice.domain.stats.service;

import com.doubleo.passservice.domain.stats.dto.response.DailyStatsInfoResponse;
import com.doubleo.passservice.domain.stats.repository.EntryStatsDailyRepository;
import com.doubleo.passservice.global.util.TenantValidator;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final EntryStatsDailyRepository entryStatsDailyRepository;
    private final TenantValidator tenantValidator;

    @Override
    public List<DailyStatsInfoResponse> getDailyPeriodStatsList() {

        String tenantId = tenantValidator.getTenantId();

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(15);

        return entryStatsDailyRepository.findDailyEnteredSumByDate(tenantId, today, startDate);
    }
}
