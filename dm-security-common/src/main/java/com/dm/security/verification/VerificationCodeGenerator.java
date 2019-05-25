package com.dm.security.verification;

public interface VerificationCodeGenerator {
	/**
	 * 生成指定长度的验证码
	 * 
	 * @param i
	 * @return
	 */
	public VerificationCode generate(int i);
}
