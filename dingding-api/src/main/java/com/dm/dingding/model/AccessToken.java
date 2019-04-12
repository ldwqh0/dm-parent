package com.dm.dingding.model;

import java.time.ZonedDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AccessToken extends DingTalkResponse {

	@JsonProperty(value = "expires_in")
	private Long expiresIn;

	@JsonProperty("access_token")
	private String accessToken;

	/**
	 * token过期时间
	 */
	private ZonedDateTime expireDate;

	public void setExpiresIn(Long expirseIn) {
		this.expiresIn = expirseIn;
		if (!Objects.isNull(expirseIn)) {
			setExpireDate(ZonedDateTime.now().plusSeconds(expirseIn));
		}
	}

	private void setExpireDate(ZonedDateTime expireDate) {
		this.expireDate = expireDate;
	}
}
