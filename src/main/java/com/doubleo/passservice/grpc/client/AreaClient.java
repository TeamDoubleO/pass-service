package com.doubleo.passservice.grpc.client;

import com.doubleo.hospitalservice.domain.area.grpc.server.*;
import com.doubleo.passservice.global.exception.CommonException;
import com.doubleo.passservice.global.exception.errorcode.GrpcErrorCode;
import com.doubleo.passservice.global.util.TenantValidator;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AreaClient {

    private final TenantValidator tenantValidator;

    @GrpcClient("hospital-service")
    private AreaServiceGrpc.AreaServiceBlockingStub blockingStub;

    public AreaClient(TenantValidator tenantValidator) {
        this.tenantValidator = tenantValidator;
    }

    public AreaResponse getAreaById(Long areaId) {
        try {
            AreaIdRequest request = AreaIdRequest.newBuilder().setAreaId(areaId).build();
            return blockingStub.getAreaById(request);
        } catch (StatusRuntimeException e) {
            log.warn(e.getMessage());
            throw new CommonException(GrpcErrorCode.GRPC_SERVER_RESPONSE_FAILED);
        }
    }

    public AreaFullNameResponse getAreaFullNameByCode(String areaCode) {
        try {
            AreaFullNameRequest request =
                    AreaFullNameRequest.newBuilder()
                            .setTenantId(tenantValidator.getTenantId())
                            .setAreaCode(areaCode)
                            .build();
            return blockingStub.getAreaFullNameByCode(request);
        } catch (StatusRuntimeException e) {
            log.warn(e.getMessage());
            throw new CommonException(GrpcErrorCode.GRPC_SERVER_RESPONSE_FAILED);
        }
    }
}
