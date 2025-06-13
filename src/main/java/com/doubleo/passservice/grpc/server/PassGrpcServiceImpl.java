package com.doubleo.passservice.grpc.server;

import com.doubleo.passservice.domain.pass.domain.Pass;
import com.doubleo.passservice.domain.pass.enums.IssuanceStatus;
import com.doubleo.passservice.domain.pass.repository.PassRepository;
import com.doubleo.passservice.global.exception.CommonException;
import com.doubleo.passservice.global.exception.errorcode.PassErrorCode;
import com.doubleo.passservice.grpc.server.PassServiceGrpc.PassServiceImplBase;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PassGrpcServiceImpl extends PassServiceImplBase {

    private final PassRepository passRepository;

    @Override
    public void updateConnectionState(
            UpdateConnectionStatusRequest request,
            StreamObserver<UpdateConnectionStatusResponse> responseObserver) {
        String tenantId = request.getTenantId();
        long passId = request.getPassId();
        String connectionId = request.getConnectionId();

        UpdateConnectionStatusResponse response;

        try {
            Pass pass =
                    passRepository
                            .findById(passId)
                            .orElseThrow(() -> new CommonException(PassErrorCode.PASS_NOT_FOUND));

            pass.updateDidConnectionId(connectionId);
            pass.updateStatus(IssuanceStatus.ISSUED);
            passRepository.save(pass);

            response =
                    UpdateConnectionStatusResponse.newBuilder()
                            .setTenantId(tenantId)
                            .setPassId(pass.getId())
                            .setMemberId(pass.getMemberId())
                            .setConnectionId(connectionId)
                            .setIsUpdated(true)
                            .build();

        } catch (Exception e) {
            log.warn("updateConnectionState 실패: {}", e.getMessage());
            response =
                    UpdateConnectionStatusResponse.newBuilder()
                            .setTenantId(tenantId)
                            .setPassId(passId)
                            .setConnectionId(connectionId)
                            .setIsUpdated(false)
                            .build();
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
