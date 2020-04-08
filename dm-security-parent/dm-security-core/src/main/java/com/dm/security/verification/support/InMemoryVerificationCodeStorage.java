package com.dm.security.verification.support;

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
        if (StringUtils.isNotBlank(id)) {
            return storage.get(id);
        } else {
            return storage.get(DEFAULT_KEY);
        }
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

}
