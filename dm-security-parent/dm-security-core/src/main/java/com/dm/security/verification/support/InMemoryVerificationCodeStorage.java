package com.dm.security.verification.support;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.dm.security.verification.VerificationCode;
import com.dm.security.verification.VerificationCodeStorage;

public class InMemoryVerificationCodeStorage implements VerificationCodeStorage {

    private static final String DEFAULT_KEY = "default_key";

    private final ConcurrentHashMap<String, VerificationCode> storage;

    public InMemoryVerificationCodeStorage() {
        this.storage = new ConcurrentHashMap<String, VerificationCode>();
    }

    @Override
    public VerificationCode get(String id) {
        return storage.get(id);
    }

    @Override
    public VerificationCode get() {
        return storage.get(DEFAULT_KEY);
    }

    @Override
    public void save(VerificationCode code) {
        String id = code.getId();
        if (StringUtils.isBlank(id)) {
            id = DEFAULT_KEY;
        }
        storage.put(id, code);
    }

    @Override
    public VerificationCode remove(String id) {
        return storage.remove(id);
    }

    @Override
    public boolean validate(String id, String code) {
        boolean result = check(id, code);
        if (result) {
            storage.remove(id);
        }
        return result;
    }

    private boolean check(String id, String code) {
        try {
            VerificationCode storeCode;
            if (StringUtils.isBlank(id)) {
                storeCode = storage.get(DEFAULT_KEY);
            } else {
                storeCode = storage.get(id);
            }
            if (Objects.isNull(storeCode)) {
                return false;
            } else {
                return StringUtils.equals(code, storeCode.getCode()) &&
                        storeCode.getInvalidateTime().isAfter(ZonedDateTime.now());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
