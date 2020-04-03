package com.dm.security.verification;

import java.util.Optional;

public interface ThreePartToolVerificationCodeStorage {

    public void remove(String verifyId);

    public Optional<ThreePartToolVerificationCode> findById(String id);

    public ThreePartToolVerificationCode save(ThreePartToolVerificationCode code);

    public Optional<ThreePartToolVerificationCode> findTopByKeyOrderByCreatedDateDesc(String key);

}
