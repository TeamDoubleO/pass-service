package com.doubleo.passservice.grpc.client;

import com.doubleo.didagent.grpc.server.AcapyServiceGrpc;
import com.doubleo.didagent.grpc.server.VcIssueRequest;
import com.doubleo.didagent.grpc.server.VcIssueResponse;
import com.doubleo.hospitalservice.domain.area.grpc.server.*;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AcapyClient {

    @GrpcClient("didagent-service")
    private AcapyServiceGrpc.AcapyServiceBlockingStub blockingStub;

    public boolean issueVc(String tenantId, Long passId, Long memberId) {
        VcIssueRequest request =
                VcIssueRequest.newBuilder()
                        .setTenantId(tenantId)
                        .setPassId(passId)
                        .setMemberId(memberId)
                        .build();

        VcIssueResponse response = blockingStub.issueVc(request);
        return response.getIsInvitationCreated();
    }
}
