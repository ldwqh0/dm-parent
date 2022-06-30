package com.dm.security.verification;

public interface DeviceVerificationCodeSender {
    void send(DeviceVerificationCode code);
}
