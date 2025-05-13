package com.doubleo.passservice.domain.log.service;

import com.doubleo.passservice.domain.log.dto.response.EnterLogResponse;
import com.doubleo.passservice.domain.log.dto.response.IssuedLogResponse;
import java.util.List;

public interface LogService {
    List<IssuedLogResponse> getAllIssuedLog();

    List<EnterLogResponse> getAllEnterLog();
}
