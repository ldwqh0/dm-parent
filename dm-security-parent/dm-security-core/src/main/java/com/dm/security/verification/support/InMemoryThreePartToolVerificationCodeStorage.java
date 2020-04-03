package com.dm.security.verification.support;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections4.CollectionUtils;

import com.dm.security.verification.ThreePartToolVerificationCode;
import com.dm.security.verification.ThreePartToolVerificationCodeStorage;

public class InMemoryThreePartToolVerificationCodeStorage implements ThreePartToolVerificationCodeStorage {

    private final Map<String, ThreePartToolVerificationCode> idKeyMap = new ConcurrentHashMap<>();

    private final Map<String, Set<ThreePartToolVerificationCode>> keyMap = new ConcurrentHashMap<>();

    @Override
    public synchronized ThreePartToolVerificationCode save(ThreePartToolVerificationCode code) {
        String id = code.getId();
        String key = code.getKey();
        idKeyMap.put(id, code);
        Set<ThreePartToolVerificationCode> keySet = keyMap.get(key);
        if (CollectionUtils.isEmpty(keySet)) {
            keySet = new HashSet<>();
            keyMap.put(key, keySet);
        }
        keySet.add(code);
        return code;
    }

    @Override
    public synchronized void remove(String verifyId) {
        ThreePartToolVerificationCode code = idKeyMap.remove(verifyId);
        if (!Objects.isNull(code)) {
            String key = code.getKey();
            Set<ThreePartToolVerificationCode> keySet = keyMap.get(key);
            if (CollectionUtils.isNotEmpty(keySet)) {
                keySet.remove(code);
            }
        }
    }

    @Override
    public Optional<ThreePartToolVerificationCode> findById(String id) {
        return Optional.<ThreePartToolVerificationCode>ofNullable(idKeyMap.get(id));
    }

    @Override
    public Optional<ThreePartToolVerificationCode> findTopByKeyOrderByCreatedDateDesc(String key) {
        Set<ThreePartToolVerificationCode> codes = keyMap.get(key);
        ZonedDateTime max = null;
        ThreePartToolVerificationCode c = null;
        if (CollectionUtils.isNotEmpty(codes)) {
            for (ThreePartToolVerificationCode code : codes) {
                ZonedDateTime currentDate = code.getCreatedDate();
                if (Objects.isNull(max) || currentDate.isAfter(max)) {
                    max = currentDate;
                    c = code;
                }
            }
        }
        return Optional.<ThreePartToolVerificationCode>ofNullable(c);
    }

}
