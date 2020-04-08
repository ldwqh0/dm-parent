package com.dm.security.verification;

import java.util.Optional;

/**
 * 第三方工具验证码服务
 * 
 * @author LiDong
 *
 */
public interface DeviceVerificationCodeStorage {

    public void remove(String verifyId);

    public Optional<DeviceVerificationCode> findById(String id);

    public DeviceVerificationCode save(DeviceVerificationCode code);

    public Optional<DeviceVerificationCode> findTopByKeyOrderByCreatedDateDesc(String key);

}
