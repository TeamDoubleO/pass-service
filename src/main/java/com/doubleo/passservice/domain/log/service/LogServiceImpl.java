package com.doubleo.passservice.domain.log.service;

import com.doubleo.passservice.domain.log.domain.EnterLog;
import com.doubleo.passservice.domain.log.domain.IssuedLog;
import com.doubleo.passservice.domain.log.dto.response.EnterLogResponse;
import com.doubleo.passservice.domain.log.dto.response.IssuedLogResponse;
import com.doubleo.passservice.domain.log.repository.EnterLogRepository;
import com.doubleo.passservice.domain.log.repository.IssuedLogRepository;
import com.doubleo.passservice.global.util.TenantValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final EnterLogRepository enterLogRepository;
    private final IssuedLogRepository issuedLogRepository;
    private final TenantValidator tenantValidator;

    @Override
    public List<IssuedLogResponse> getAllIssuedLog() {
        String tenantId = tenantValidator.getTenantId();
        List<IssuedLog> issuedLogs = issuedLogRepository.findAllByTenantId(tenantId);
        return issuedLogs.stream()
                .map(
                        issuedLog ->
                                new IssuedLogResponse(
                                        issuedLog.getMemberId(),
                                        issuedLog.getMemberName(),
                                        issuedLog.getPassId(),
                                        issuedLog.getStartAt(),
                                        issuedLog.getExpiredAt(),
                                        issuedLog.getVisitCategory()))
                .toList();
    }

    @Override
    public List<EnterLogResponse> getAllEnterLog() {
        String tenantId = tenantValidator.getTenantId();
        List<EnterLog> enterLogs = enterLogRepository.findAllByTenantId(tenantId);
        return enterLogs.stream()
                .map(
                        enterLog ->
                                new EnterLogResponse(
                                        enterLog.getAreaId(),
                                        enterLog.getMemberId(),
                                        enterLog.getMemberName(),
                                        enterLog.getPassId()))
                .toList();
    }
}
