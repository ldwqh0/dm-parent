package com.dm.springboot.autoconfigure.security;

import com.dm.security.verification.DeviceVerificationCodeSender;
import com.dm.security.verification.DeviceVerificationCodeService;
import com.dm.security.verification.DeviceVerificationCodeStorage;
import com.dm.security.verification.support.DefaultDeviceVerificationCodeService;
import com.dm.security.verification.support.InMemoryDeviceVerificationCodeStorage;
import com.dm.security.verification.support.Slf4jDeviceVerificationCodeSender;
import com.dm.security.web.verification.controller.DeviceVerificationCodeController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

public class DeviceVerificationCodeConfiguration {

    @Bean
    @ConditionalOnMissingBean(DeviceVerificationCodeStorage.class)
    public DeviceVerificationCodeStorage deviceVerificationCodeStorage() {
        return new InMemoryDeviceVerificationCodeStorage();
    }

    @Bean
    @ConditionalOnMissingBean(DeviceVerificationCodeService.class)
    public DeviceVerificationCodeService deviceVerificationCodeService(DeviceVerificationCodeStorage deviceVerificationCodeStorage) {
        return new DefaultDeviceVerificationCodeService(deviceVerificationCodeStorage);
    }

    @Bean
    @ConditionalOnMissingBean(DeviceVerificationCodeSender.class)
    public DeviceVerificationCodeSender deviceVerificationCodeSender() {
        return new Slf4jDeviceVerificationCodeSender();
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public DeviceVerificationCodeController deviceVerificationCodeController(DeviceVerificationCodeService deviceVerificationCodeService, DeviceVerificationCodeSender codeSender) {
        return new DeviceVerificationCodeController(deviceVerificationCodeService, codeSender);
    }

}
