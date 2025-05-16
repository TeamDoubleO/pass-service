package com.doubleo.passservice.domain.log.service;

import com.doubleo.passservice.domain.log.domain.EnterLog;
import com.doubleo.passservice.domain.log.domain.IssuedLog;
import com.doubleo.passservice.domain.log.dto.response.EnterLogResponse;
import com.doubleo.passservice.domain.log.dto.response.IssuedLogResponse;
import com.doubleo.passservice.domain.log.repository.EnterLogRepository;
import com.doubleo.passservice.domain.log.repository.IssuedLogAreaRepository;
import com.doubleo.passservice.domain.log.repository.IssuedLogRepository;
import com.doubleo.passservice.global.util.TenantValidator;
import com.doubleo.passservice.grpc.client.AreaClient;
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
    private final IssuedLogAreaRepository issuedLogAreaRepository;
    private final TenantValidator tenantValidator;
    private final AreaClient areaClient;

    @Override
    public Page<IssuedLogResponse> getAllIssuedLog(Pageable pageable) {
        String tenantId = tenantValidator.getTenantId();
        Page<IssuedLog> issuedLogs = issuedLogRepository.findAllByTenantId(tenantId, pageable);

        return issuedLogs.map(
                issuedLog -> {
                    List<String> areaCodes =
                            issuedLogAreaRepository.findAreaCodesByIssuedLog(issuedLog);
                    List<String> areaNames =
                            areaCodes.stream()
                                    .map(
                                            code ->
                                                    areaClient
                                                            .getAreaFullNameByCode(code)
                                                            .getAreaFullName())
                                    .toList();

                    return new IssuedLogResponse(
                            issuedLog.getMemberId(),
                            issuedLog.getMemberName(),
                            issuedLog.getPassId(),
                            areaNames,
                            issuedLog.getStartAt(),
                            issuedLog.getExpiredAt(),
                            issuedLog.getVisitCategory());
                });
    }

    @Override
    public Page<EnterLogResponse> getAllEnterLog(Pageable pageable) {
        String tenantId = tenantValidator.getTenantId();
        Page<EnterLog> enterLogs = enterLogRepository.findAllByTenantId(tenantId, pageable);
        return enterLogs.map(
                enterLog ->
                        new EnterLogResponse(
                                enterLog.getAreaId(),
                                areaClient
                                        .getAreaFullNameByCode(
                                                areaClient
                                                        .getAreaById(enterLog.getAreaId())
                                                        .getAreaCode())
                                        .getAreaFullName(),
                                enterLog.getMemberId(),
                                enterLog.getMemberName(),
                                enterLog.getPassId(),
                                enterLog.getCreatedDt()));
    }
}
