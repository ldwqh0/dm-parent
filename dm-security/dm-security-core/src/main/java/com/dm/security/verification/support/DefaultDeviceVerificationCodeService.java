package com.dm.security.verification.support;

import com.dm.security.verification.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;

/**
 * 默认的设备验证码服务
 */
public class DefaultDeviceVerificationCodeService implements DeviceVerificationCodeService {

    private static final Logger log = LoggerFactory.getLogger(DefaultDeviceVerificationCodeService.class);
    private final DeviceVerificationCodeStorage codeStorage;

    private final ConcurrencyStrategy concurrencyStrategy;

    public DefaultDeviceVerificationCodeService(DeviceVerificationCodeStorage codeStorage) {
        this.codeStorage = codeStorage;
        this.concurrencyStrategy = new StorageBaseConcurrencyStrategy(codeStorage);
    }

    public DefaultDeviceVerificationCodeService(DeviceVerificationCodeStorage codeStorage, ConcurrencyStrategy concurrencyStrategy) {
        this.codeStorage = codeStorage;
        this.concurrencyStrategy = concurrencyStrategy;
    }

    @Override
    public DeviceVerificationCode create(DeviceInfo device) {
        String deviceKey = device.getDeviceKey();
        concurrencyStrategy.tryLock(device);
        DeviceVerificationCode generatedCode = new DeviceVerificationCode(deviceKey, RandomStringUtils.random(6, "0123456789"));
        log.debug("create a new DeviceVerificationCode [{}]", generatedCode);
        return codeStorage.save(generatedCode);
    }

    @Override
    public boolean validate(String verifyId, String key, String verifyCode) {
        boolean result = codeStorage.findById(verifyId)
            .map(savedItem -> ZonedDateTime.now().isBefore(savedItem.getExpireAt())
                && StringUtils.equals(key, savedItem.getKey())
                && StringUtils.equalsIgnoreCase(verifyCode, savedItem.getCode()))
            .orElse(false);
        if (result) {
            codeStorage.remove(verifyId);
        }
        return result;
    }
}
