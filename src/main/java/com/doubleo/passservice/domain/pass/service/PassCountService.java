package com.doubleo.passservice.domain.pass.service;

import com.doubleo.passservice.domain.pass.dto.request.PassCountInfoRequest;
import com.doubleo.passservice.domain.pass.dto.response.PassCountInfoResponse;
import java.util.List;

public interface PassCountService {
    List<PassCountInfoResponse> getPassCount(PassCountInfoRequest request);
}
