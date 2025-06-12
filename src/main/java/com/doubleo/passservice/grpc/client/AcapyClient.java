package com.doubleo.passservice.grpc.client;

import com.doubleo.didagent.grpc.server.AcapyServiceGrpc;
import com.doubleo.didagent.grpc.server.PassConnectionIdRequest;
import com.doubleo.didagent.grpc.server.PassConnectionIdResponse;
import com.doubleo.hospitalservice.domain.area.grpc.server.*;
import com.doubleo.passservice.global.exception.GrpcExceptionUtil;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AcapyClient {

    @GrpcClient("acapy-service")
    private AcapyServiceGrpc.AcapyServiceBlockingStub blockingStub;

    public String getPassConnectionId(String tenantId, Long memberId) {
        try {
            PassConnectionIdRequest request =
                    PassConnectionIdRequest.newBuilder()
                            .setTenantId(tenantId)
                            .setMemberId(memberId)
                            .build();

            PassConnectionIdResponse response = blockingStub.getPassConnectionId(request);
            return response.getPassConnectionId();
        } catch (StatusRuntimeException e) {
            log.warn("gRPC 요청 실패: {}", e.getMessage());
            throw GrpcExceptionUtil.fromStatusRuntimeException(e);
        }
    }
}
