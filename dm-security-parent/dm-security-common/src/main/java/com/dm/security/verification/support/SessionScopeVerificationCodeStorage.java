package com.dm.security.verification.support;

import java.time.ZonedDateTime;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.dm.security.verification.VerificationCode;
import com.dm.security.verification.VerificationCodeStorage;

/**
 * 基于session保存的 {@link VerificationCodeStorage}<br>
 * 需要将这个Storage配置为sessionScope Bean <br>
 * 所有相关的校验都会忽略id属性
 * 
 * @author LiDong
 *
 */
public class SessionScopeVerificationCodeStorage implements VerificationCodeStorage {

	private VerificationCode storageCode;

	@Override
	public VerificationCode get(String id) {
		return storageCode;
	}

	@Override
	public VerificationCode get() {
		return storageCode;
	}

	@Override
	public void save(VerificationCode code) {
		this.storageCode = code;
	}

	@Override
	public VerificationCode remove(String id) {
		VerificationCode temp = this.storageCode;
		this.storageCode = null;
		return temp;
	}

	@Override
	public boolean validate(String id, String code) {
		if (Objects.isNull(this.storageCode)) {
			return false;
		} else {
			return StringUtils.equals(code, storageCode.getCode()) &&
					storageCode.getInvalidateTime().isAfter(ZonedDateTime.now());
		}
	}

}
