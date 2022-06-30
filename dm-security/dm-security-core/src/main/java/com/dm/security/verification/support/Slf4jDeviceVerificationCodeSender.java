package com.dm.security.verification.support;

import com.dm.security.verification.DeviceVerificationCode;
import com.dm.security.verification.DeviceVerificationCodeSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jDeviceVerificationCodeSender implements DeviceVerificationCodeSender {
    private static final Logger log = LoggerFactory.getLogger(Slf4jDeviceVerificationCodeSender.class);

    @Override
    public void send(DeviceVerificationCode code) {
        log.info("send message to device [{}],code id=[{}],code=[{}],expire at [{}]", code.getKey(), code.getId(), code.getCode(), code.getExpireAt());
    }
}
