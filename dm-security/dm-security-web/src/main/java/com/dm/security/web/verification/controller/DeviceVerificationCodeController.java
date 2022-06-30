package com.dm.security.web.verification.controller;

import com.dm.security.verification.DeviceVerificationCode;
import com.dm.security.verification.DeviceVerificationCodeSender;
import com.dm.security.verification.DeviceVerificationCodeService;
import com.dm.security.web.verification.request.DeviceVerificationCodeRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("deviceVerificationCodes")
public class DeviceVerificationCodeController {

    private final DeviceVerificationCodeService codeService;

    private final DeviceVerificationCodeSender codeSender;

    public DeviceVerificationCodeController(DeviceVerificationCodeService codeService, DeviceVerificationCodeSender codeSender) {
        this.codeService = codeService;
        this.codeSender = codeSender;
    }

    /**
     * 获取-创建一个新的验证码
     *
     * @param request 验证码请求
     */
    @PostMapping
    public DeviceVerificationCode newCode(@RequestBody DeviceVerificationCodeRequest request) {
        DeviceVerificationCode code = codeService.create(request);
        codeSender.send(code);
        return code;
    }

}
