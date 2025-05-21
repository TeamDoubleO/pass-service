package com.doubleo.passservice.domain.log.service;

import com.doubleo.passservice.domain.log.dto.response.HourlyEntryResponse;
import com.doubleo.passservice.domain.log.repository.BuildingEnterLogRepository;
import com.doubleo.passservice.global.util.TenantValidator;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final RedisTemplate<String, String> redisTemplate;
    private final TenantValidator tenantValidator;
    private final BuildingEnterLogRepository buildingEnterLogRepository;

    @Override
    public List<HourlyEntryResponse> getHourlyEntryList() {
        LocalDateTime now = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
        String tenantId = tenantValidator.getTenantId();

        LocalDateTime end = now;
        LocalDateTime start = end.minusHours(24);

        List<HourlyEntryResponse> hourlyList = new ArrayList<>();

        for (int i = 0; i < 24; i++) {
            LocalDateTime hour = start.plusHours(i);
            String hourStr = hour.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH"));
            String redisKey = "hourlyEntry:" + tenantId + ":" + hourStr;

            String cachedCount = redisTemplate.opsForValue().get(redisKey);

            int total;
            if (cachedCount != null) {
                total = Integer.parseInt(cachedCount);
            } else {
                int count = buildingEnterLogRepository.countInLogsAtHour(hour, hour.plusHours(1));
                redisTemplate
                        .opsForValue()
                        .set(redisKey, String.valueOf(count), Duration.ofSeconds(10)); // TTL 설정
                total = count;
            }

            hourlyList.add(
                    new HourlyEntryResponse(String.format("%02d시", hour.getHour()), total, hour));
        }

        return hourlyList;
    }
}
