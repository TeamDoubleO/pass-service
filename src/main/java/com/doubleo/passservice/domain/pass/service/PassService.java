package com.doubleo.passservice.domain.pass.service;

import com.doubleo.passservice.domain.pass.dto.response.MemberPassInfoResponse;
import java.util.List;

public interface PassService {
    List<MemberPassInfoResponse> getAllMemberPassInfo(Long memberId);
}
