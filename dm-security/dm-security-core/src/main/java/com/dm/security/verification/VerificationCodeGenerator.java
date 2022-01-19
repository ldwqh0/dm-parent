package com.dm.security.verification;

public interface VerificationCodeGenerator {
    /**
     * 生成指定长度的验证码
     *
     * @param i
     */
    VerificationCode generate(int i);
}
