package com.dm.security.verification.support;

import com.dm.security.verification.ConcurrencyException;
import com.dm.security.verification.ConcurrencyStrategy;
import com.dm.security.verification.DeviceInfo;
import com.dm.security.verification.DeviceVerificationCodeStorage;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class StorageBaseConcurrencyStrategy implements ConcurrencyStrategy {
    private final DeviceVerificationCodeStorage codeStorage;

    private final Interner<String> keyInterner = Interners.newWeakInterner();

    public StorageBaseConcurrencyStrategy(DeviceVerificationCodeStorage codeStorage) {
        this.codeStorage = codeStorage;
    }

    @Override
    public void tryLock(DeviceInfo device) {
        String deviceKey = device.getDeviceKey();
        synchronized (keyInterner.intern(deviceKey)) {
            codeStorage.findTopByKeyOrderByCreatedDateDesc(deviceKey)
                .filter(it -> it.getCreatedTime().plus(1, ChronoUnit.MINUTES).isBefore(ZonedDateTime.now()))
                .ifPresent(it -> {
                    throw new ConcurrencyException();
                });
        }
    }
}
