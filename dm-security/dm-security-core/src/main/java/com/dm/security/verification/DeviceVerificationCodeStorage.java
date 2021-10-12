package com.dm.security.verification;

import java.util.Optional;

/**
 * 第三方工具验证码服务
 *
 * @author LiDong
 */
public interface DeviceVerificationCodeStorage {

    void remove(String verifyId);

    Optional<DeviceVerificationCode> findById(String id);

    DeviceVerificationCode save(DeviceVerificationCode code);

    Optional<DeviceVerificationCode> findTopByKeyOrderByCreatedDateDesc(String key);

}
