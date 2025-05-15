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
            List<List<String>> areaNames = new ArrayList<>();
            for (PassArea passArea : passAreas) {
                List<String> areaName = buildAreaName(passArea.getAreaCode());
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

    private List<String> buildAreaName(String areaCode) {
        String[] parts = areaCode.split("_");
        List<String> areaName = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            if (i > 0) sb.append("_");
            sb.append(parts[i]);
            areaName.add(sb.toString());
        }
        // 구역 코드로 구역 이름 찾는 로직 추가 해야 함
        return areaName;
    }
}
