package com.dm.security.verification;

public interface VerificationCodeStorage {

    /**
     * 根据ID获取验证码信息
     *
     * @param id 根据id获取验证码
     * @return 获取到的验证码
     */
    VerificationCode get(String id);

    /**
     * 获取默认的验证码信息
     *
     * @return 获取到的验证码
     */
    VerificationCode get();

    /**
     * 保存验证码信息
     *
     * @param code 要保存的验证码
     */
    void save(VerificationCode code);

    /**
     * 从存储中移除验证码
     *
     * @param id 要移除的验证码的id
     * @return 移除的验证码
     */
    VerificationCode remove(String id);
}
