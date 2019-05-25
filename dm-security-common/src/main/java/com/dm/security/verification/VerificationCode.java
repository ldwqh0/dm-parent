package com.dm.security.verification;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class VerificationCode {

	/**
	 * 验证码默认的失效时常，单位是分钟
	 */
	private static final int DEFAULT_INVALIDATE_TIME = 5;

	private String id;

	@JsonProperty(access = Access.WRITE_ONLY)
	private String code;

	private ZonedDateTime invalidateTime;
	private String imgData;

	public VerificationCode(String id, String code, ZonedDateTime invalidateTime) {
		super();
		this.id = id;
		this.code = code;
		this.invalidateTime = invalidateTime;
	}

	public VerificationCode(String id, String code) {
		super();
		this.id = id;
		this.code = code;
		this.invalidateTime = ZonedDateTime.now().plusMinutes(DEFAULT_INVALIDATE_TIME);
	}

	protected VerificationCode() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ZonedDateTime getInvalidateTime() {
		return invalidateTime;
	}

	public void setInvalidateTime(ZonedDateTime invalidateTime) {
		this.invalidateTime = invalidateTime;
	}

	public String getImgData() {
		return imgData;
	}

	public void setImgData(String imgData) {
		this.imgData = imgData;
	}

}
