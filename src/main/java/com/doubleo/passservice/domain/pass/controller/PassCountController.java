package com.doubleo.passservice.domain.pass.controller;

import com.doubleo.passservice.domain.pass.dto.request.PassCountInfoRequest;
import com.doubleo.passservice.domain.pass.dto.response.PassCountInfoResponse;
import com.doubleo.passservice.domain.pass.service.PassCountService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/passes")
@RequiredArgsConstructor
public class PassCountController {

    private final PassCountService passCountService;

    @PostMapping("/search")
    public List<PassCountInfoResponse> passCountListGet(
            @RequestBody @Valid PassCountInfoRequest request) {
        System.out.println("컨트롤러 getPassCount 호출됨");
        return passCountService.getPassCount(request);
    }
}
