package com.doubleo.passservice.grpc.client;

import com.doubleo.memberservice.domain.member.grpc.server.MemberRequest;
import com.doubleo.memberservice.domain.member.grpc.server.MemberResponse;
import com.doubleo.memberservice.domain.member.grpc.server.MemberServiceGrpc;
import com.doubleo.passservice.global.exception.CommonException;
import com.doubleo.passservice.global.exception.errorcode.GrpcErrorCode;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MemberClient {

    @GrpcClient("member-service")
    private MemberServiceGrpc.MemberServiceBlockingStub blockingStub;

    public MemberResponse getMemberById(Long memberId) {
        try {
            MemberRequest request = MemberRequest.newBuilder().setMemberId(memberId).build();
            return blockingStub.getMember(request);
        } catch (StatusRuntimeException e) {
            log.error(e.getMessage());
            throw new CommonException(GrpcErrorCode.GRPC_SERVER_RESPONSE_FAILED);
        }
    }
}
