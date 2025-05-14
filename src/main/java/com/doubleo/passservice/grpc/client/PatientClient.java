package com.doubleo.passservice.grpc.client;

import com.doubleo.passservice.global.exception.CommonException;
import com.doubleo.passservice.global.exception.errorcode.GrpcErrorCode;
import com.doubleo.patientservice.domain.patient.grpc.server.PatientRequest;
import com.doubleo.patientservice.domain.patient.grpc.server.PatientResponse;
import com.doubleo.patientservice.domain.patient.grpc.server.PatientServiceGrpc;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PatientClient {

    @GrpcClient("patient-service")
    private PatientServiceGrpc.PatientServiceBlockingStub blockingStub;

    public PatientResponse getPatientById(Long id) {
        try {
            PatientRequest request = PatientRequest.newBuilder().setPatientId(id).build();
            return blockingStub.getPatient(request);
        } catch (StatusRuntimeException e) {
            log.error(e.getMessage());
            throw new CommonException(GrpcErrorCode.GRPC_SERVER_RESPONSE_FAILED);
        }
    }
}
