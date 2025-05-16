package com.doubleo.passservice.domain.pass.service;

import com.doubleo.passservice.domain.pass.domain.Pass;
import com.doubleo.passservice.domain.pass.domain.PassArea;
import com.doubleo.passservice.domain.pass.dto.response.MemberPassInfoResponse;
import com.doubleo.passservice.domain.pass.repository.PassAreaRepository;
import com.doubleo.passservice.domain.pass.repository.PassRepository;
import com.doubleo.passservice.grpc.client.AreaClient;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PassServiceImpl implements PassService {

    private final PassRepository passRepository;
    private final PassAreaRepository passAreaRepository;
    private final AreaClient areaClient;

    @Override
    public List<MemberPassInfoResponse> getAllMemberPassInfo(Long memberId) {
        List<Pass> passes = passRepository.findAllByMemberId(memberId);
        List<MemberPassInfoResponse> responses = new ArrayList<>();
        for (Pass pass : passes) {
            List<PassArea> passAreas = passAreaRepository.findAllByPass(pass);
            List<String> areaNames = new ArrayList<>();
            for (PassArea passArea : passAreas) {
                String areaName =
                        areaClient.getAreaFullNameByCode(passArea.getAreaCode()).getAreaFullName();
                areaNames.add(areaName);
            }
            MemberPassInfoResponse response =
                    new MemberPassInfoResponse(
                            pass.getId(),
                            pass.getMemberId(),
                            pass.getHospitalId(),
                            areaNames,
                            pass.getVisitCategory(),
                            pass.getPatientId(),
                            pass.getStartAt(),
                            pass.getExpiredAt());
            responses.add(response);
        }
        return responses;
    }
}
