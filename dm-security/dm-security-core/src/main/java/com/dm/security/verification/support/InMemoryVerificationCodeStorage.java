package com.dm.security.verification.support;

import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;

import com.dm.security.verification.VerificationCode;
import com.dm.security.verification.VerificationCodeStorage;

public class InMemoryVerificationCodeStorage implements VerificationCodeStorage {

    private static final String DEFAULT_KEY = "default_key";

    private final ConcurrentHashMap<String, VerificationCode> storage;

    public InMemoryVerificationCodeStorage() {
        this.storage = new ConcurrentHashMap<>();
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

    /**
     * 一分钟清理一次内存
     */
    @Scheduled(fixedRate = 1000 * 60)
    public synchronized void autoClean() {
        ZonedDateTime now = ZonedDateTime.now();
        Iterator<Entry<String, VerificationCode>> it = storage.entrySet().iterator();
        while (it.hasNext()) {
            VerificationCode code = it.next().getValue();
            if (now.isAfter(code.getInvalidateTime())) {
                it.remove();
            }
        }
    }

}
