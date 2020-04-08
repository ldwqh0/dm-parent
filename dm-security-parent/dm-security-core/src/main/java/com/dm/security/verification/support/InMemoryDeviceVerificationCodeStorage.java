package com.dm.security.verification.support;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections4.CollectionUtils;

import com.dm.security.verification.DeviceVerificationCode;
import com.dm.security.verification.DeviceVerificationCodeStorage;

public class InMemoryDeviceVerificationCodeStorage implements DeviceVerificationCodeStorage {

    private final Map<String, DeviceVerificationCode> idKeyMap = new ConcurrentHashMap<>();

    private final Map<String, Set<DeviceVerificationCode>> keyMap = new ConcurrentHashMap<>();

    @Override
    public synchronized DeviceVerificationCode save(DeviceVerificationCode code) {
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
    public synchronized void remove(String verifyId) {
        DeviceVerificationCode code = idKeyMap.remove(verifyId);
        if (!Objects.isNull(code)) {
            String key = code.getKey();
            Set<DeviceVerificationCode> keySet = keyMap.get(key);
            if (CollectionUtils.isNotEmpty(keySet)) {
                keySet.remove(code);
            }
        }
    }

    @Override
    public Optional<DeviceVerificationCode> findById(String id) {
        return Optional.<DeviceVerificationCode>ofNullable(idKeyMap.get(id));
    }

    @Override
    public Optional<DeviceVerificationCode> findTopByKeyOrderByCreatedDateDesc(String key) {
        Set<DeviceVerificationCode> codes = keyMap.get(key);
        ZonedDateTime max = null;
        DeviceVerificationCode c = null;
        if (CollectionUtils.isNotEmpty(codes)) {
            for (DeviceVerificationCode code : codes) {
                ZonedDateTime currentDate = code.getCreatedDate();
                if (Objects.isNull(max) || currentDate.isAfter(max)) {
                    max = currentDate;
                    c = code;
                }
            }
        }
        return Optional.<DeviceVerificationCode>ofNullable(c);
    }

}
