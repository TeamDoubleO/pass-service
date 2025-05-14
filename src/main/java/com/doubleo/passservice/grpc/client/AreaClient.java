package com.doubleo.passservice.grpc.client;

import com.doubleo.hospitalservice.domain.area.grpc.server.AreaIdRequest;
import com.doubleo.hospitalservice.domain.area.grpc.server.AreaResponse;
import com.doubleo.hospitalservice.domain.area.grpc.server.AreaServiceGrpc;
import com.doubleo.passservice.global.exception.CommonException;
import com.doubleo.passservice.global.exception.errorcode.GrpcErrorCode;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AreaClient {

    @GrpcClient("hospital-service")
    private AreaServiceGrpc.AreaServiceBlockingStub blockingStub;

    public AreaResponse getAreaById(Long areaId) {
        try {
            AreaIdRequest request = AreaIdRequest.newBuilder().setAreaId(areaId).build();
            return blockingStub.getAreaById(request);
        } catch (StatusRuntimeException e) {
            log.warn(e.getMessage());
            throw new CommonException(GrpcErrorCode.GRPC_SERVER_RESPONSE_FAILED);
        }
    }
}
