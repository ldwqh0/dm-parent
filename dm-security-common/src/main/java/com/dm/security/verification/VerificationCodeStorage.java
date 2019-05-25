package com.dm.security.verification;

import org.springframework.lang.Nullable;

public interface VerificationCodeStorage {

	/**
	 * 根据ID获取验证码信息
	 * 
	 * @param id
	 * @return
	 */
	public VerificationCode get(String id);

	/**
	 * 获取默认的验证码信息
	 * 
	 * @return
	 */
	public VerificationCode get();

	/**
	 * 保存验证码信息
	 * 
	 * @param code
	 */
	public void save(VerificationCode code);

	/**
	 * 从存储中移除验证码
	 * 
	 * @param id
	 * @return
	 */
	public VerificationCode remove(String id);

	/**
	 * 校验验证码是否正确
	 * 
	 * @param id
	 * @param code
	 * @return
	 */
	public boolean validate(@Nullable String id, String code);
}
