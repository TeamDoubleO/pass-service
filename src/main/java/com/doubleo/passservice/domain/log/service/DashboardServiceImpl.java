package com.doubleo.passservice.domain.log.service;

import com.doubleo.passservice.domain.log.domain.BuildingEnterLog;
import com.doubleo.passservice.domain.log.domain.Direction;
import com.doubleo.passservice.domain.log.domain.IssuedLog;
import com.doubleo.passservice.domain.log.dto.response.HourlyEntryResponse;
import com.doubleo.passservice.domain.log.dto.response.SummaryResponse;
import com.doubleo.passservice.domain.log.repository.BuildingEnterLogRepository;
import com.doubleo.passservice.domain.log.repository.IssuedLogRepository;
import com.doubleo.passservice.domain.pass.enums.VisitCategory;
import com.doubleo.passservice.global.util.TenantValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {
    private final RedisTemplate<String, String> redisTemplate;
    private final TenantValidator tenantValidator;
    private final BuildingEnterLogRepository buildingEnterLogRepository;
    private final IssuedLogRepository issuedLogRepository;

    @Autowired private ObjectMapper objectMapper;

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

    @Override
    public List<SummaryResponse> getTodaySummary() {
        String tenantId = tenantValidator.getTenantId();
        String cacheKey = "summary:" + tenantId + ":" + LocalDate.now();

        String cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            try {
                return objectMapper.readValue(
                        cached, new TypeReference<List<SummaryResponse>>() {});
            } catch (JsonProcessingException e) {
                log.warn(
                        "[Dashboard] Failed to deserialize cache: key={}, reason={}",
                        cacheKey,
                        e.getMessage());
            }
        }

        // 당일 입퇴장 수 계산을 위한 조회 범위
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime now = LocalDateTime.now();

        // 잔류 인원 계산용 - 최근 7일간 로그 전체 조회
        LocalDateTime baseStart = LocalDate.now().minusDays(7).atStartOfDay();
        List<BuildingEnterLog> fullLogs =
                buildingEnterLogRepository.findAllByTenantIdAndCreatedDtBetween(
                        tenantId, baseStart, now);

        List<BuildingEnterLog> logs =
                buildingEnterLogRepository.findAllByTenantIdAndCreatedDtBetween(
                        tenantId, startOfDay, now);

        int totalEntered = 0;
        int totalExited = 0;
        Map<VisitCategory, Integer> enteredMap = new EnumMap<>(VisitCategory.class);
        Map<VisitCategory, Integer> exitedMap = new EnumMap<>(VisitCategory.class);
        Map<VisitCategory, Integer> remainingMap = new EnumMap<>(VisitCategory.class);
        Map<Long, Direction> latestDirectionByMember = new HashMap<>();
        Map<Long, Long> passIdByMember = new HashMap<>();

        for (BuildingEnterLog log : logs) {
            Direction direction = log.getDirection();
            Long memberId = log.getMemberId();
            Long passId = log.getPassId();

            IssuedLog issuedLog = issuedLogRepository.findByPassId(passId).orElse(null);
            if (issuedLog == null || issuedLog.getVisitCategory() == null) continue;

            VisitCategory category = issuedLog.getVisitCategory();

            if (direction == Direction.IN) {
                enteredMap.merge(category, 1, Integer::sum);
                totalEntered++;
            } else {
                exitedMap.merge(category, 1, Integer::sum);
                totalExited++;
            }

            latestDirectionByMember.put(memberId, direction);
            passIdByMember.put(memberId, passId);
        }

        Map<Long, BuildingEnterLog> latestLogByMember = new HashMap<>();
        for (BuildingEnterLog log : fullLogs) {
            Long memberId = log.getMemberId();
            if (!latestLogByMember.containsKey(memberId)
                    || latestLogByMember
                            .get(memberId)
                            .getCreatedDt()
                            .isBefore(log.getCreatedDt())) {
                latestLogByMember.put(memberId, log);
            }
        }

        List<SummaryResponse> result = new ArrayList<>();
        for (VisitCategory category : VisitCategory.values()) {
            result.add(
                    new SummaryResponse(
                            category.name(),
                            enteredMap.getOrDefault(category, 0),
                            exitedMap.getOrDefault(category, 0),
                            remainingMap.getOrDefault(category, 0)));
        }
        result.add(
                new SummaryResponse(
                        "Total",
                        totalEntered,
                        totalExited,
                        remainingMap.values().stream().mapToInt(i -> i).sum()));

        try {
            redisTemplate
                    .opsForValue()
                    .set(
                            cacheKey,
                            objectMapper.writeValueAsString(result),
                            //                    Duration.ofMinutes(5)
                            Duration.ofSeconds(10));
        } catch (JsonProcessingException e) {
            log.warn(
                    "[Dashboard] Failed to serialize cache: key={}, reason={}",
                    cacheKey,
                    e.getMessage());
        }

        return result;
    }
}
