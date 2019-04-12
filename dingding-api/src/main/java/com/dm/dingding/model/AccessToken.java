package com.dm.dingding.model;

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

}
