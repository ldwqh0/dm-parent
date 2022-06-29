package com.dm.security.verification.support;

import com.dm.collections.CollectionUtils;
import com.dm.security.verification.DeviceVerificationCode;
import com.dm.security.verification.DeviceVerificationCodeStorage;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDeviceVerificationCodeStorage implements DeviceVerificationCodeStorage {

    // 以验证码ID为key的验证码map
    private final Map<String, DeviceVerificationCode> idKeyMap = new ConcurrentHashMap<>();

    // 以设备识别符为key的验证码map
    private final Map<String, Set<DeviceVerificationCode>> keyMap = new ConcurrentHashMap<>();

    @Override
    public DeviceVerificationCode save(DeviceVerificationCode code) {
        String id = code.getId();
        String key = code.getKey();
        idKeyMap.put(id, code);
        Set<DeviceVerificationCode> keySet = keyMap.get(key);
        if (CollectionUtils.isEmpty(keySet)) {
            keySet = new HashSet<>();
            keyMap.put(key, keySet);
        }
        keySet.add(code);
        return code;
    }

    @Override
    public void remove(String verifyId) {
        DeviceVerificationCode code = idKeyMap.remove(verifyId);
        if (Objects.nonNull(code)) {
            String key = code.getKey();
            Set<DeviceVerificationCode> keySet = keyMap.get(key);
            if (CollectionUtils.isNotEmpty(keySet)) {
                keySet.remove(code);
            }
        }
    }

    @Override
    public Optional<DeviceVerificationCode> findById(String id) {
        return Optional.ofNullable(idKeyMap.get(id));
    }

    @Override
    public Optional<DeviceVerificationCode> findTopByKeyOrderByCreatedDateDesc(String key) {
        Set<DeviceVerificationCode> codes = keyMap.get(key);
        ZonedDateTime max = null;
        DeviceVerificationCode c = null;
        if (CollectionUtils.isNotEmpty(codes)) {
            for (DeviceVerificationCode code : codes) {
                ZonedDateTime currentDate = code.getCreatedTime();
                if (Objects.isNull(max) || currentDate.isAfter(max)) {
                    max = currentDate;
                    c = code;
                }
            }
        }
        return Optional.ofNullable(c);
    }

    /**
     * 一分钟秒清理一次内存
     */
    @Scheduled(fixedRate = 1000 * 60)
    public synchronized void autoClean() {
        Set<String> keys = idKeyMap.keySet();
        ZonedDateTime now = ZonedDateTime.now();
        keys.forEach(id -> {
            DeviceVerificationCode code = idKeyMap.get(id);
            if (now.isAfter(code.getExpireAt())) {
                remove(id);
            }
        });
    }

}
