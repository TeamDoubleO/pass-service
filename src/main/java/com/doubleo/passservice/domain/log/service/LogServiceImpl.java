package com.doubleo.passservice.domain.log.service;

import com.doubleo.passservice.domain.log.domain.EnterLog;
import com.doubleo.passservice.domain.log.domain.IssuedLog;
import com.doubleo.passservice.domain.log.dto.response.EnterLogResponse;
import com.doubleo.passservice.domain.log.dto.response.IssuedLogResponse;
import com.doubleo.passservice.domain.log.repository.EnterLogRepository;
import com.doubleo.passservice.domain.log.repository.IssuedLogRepository;
import com.doubleo.passservice.global.util.TenantValidator;
import com.doubleo.passservice.grpc.client.AreaClient;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final EnterLogRepository enterLogRepository;
    private final IssuedLogRepository issuedLogRepository;
    private final TenantValidator tenantValidator;
    private final AreaClient areaClient;

    @Override
    public Page<IssuedLogResponse> getAllIssuedLog(Pageable pageable) {
        String tenantId = tenantValidator.getTenantId();
        Page<IssuedLog> issuedLogs = issuedLogRepository.findAllByTenantId(tenantId, pageable);
        return issuedLogs.map(
                issuedLog ->
                        new IssuedLogResponse(
                                issuedLog.getMemberId(),
                                issuedLog.getMemberName(),
                                issuedLog.getPassId(),
                                issuedLog.getAreaCode(),
                                buildAreaName(issuedLog.getAreaCode()),
                                issuedLog.getStartAt(),
                                issuedLog.getExpiredAt(),
                                issuedLog.getVisitCategory()));
    }

    @Override
    public Page<EnterLogResponse> getAllEnterLog(Pageable pageable) {
        String tenantId = tenantValidator.getTenantId();
        Page<EnterLog> enterLogs = enterLogRepository.findAllByTenantId(tenantId, pageable);
        return enterLogs.map(
                enterLog ->
                        new EnterLogResponse(
                                enterLog.getAreaId(),
                                areaClient.getAreaById(enterLog.getAreaId()).getAreaName(),
                                enterLog.getMemberId(),
                                enterLog.getMemberName(),
                                enterLog.getPassId(),
                                enterLog.getCreatedDt()));
    }

    private List<String> buildAreaName(String areaCode) {
        String[] parts = areaCode.split("_");
        List<String> areaName = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            if (i > 0) sb.append("_");
            sb.append(parts[i]);
            areaName.add(sb.toString());
        }
        // 구역 코드로 구역 이름 찾는 로직 추가 해야 함
        return areaName;
    }
}
