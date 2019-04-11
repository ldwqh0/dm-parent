package com.dm.dingding.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AccessToken {
	
	@JsonProperty(value = "expires_in")
	private Long expiresIn;
	
	private String errmsg;
	
	@JsonProperty("access_token")
	private String accessToken;
	
	private int errcode;
}
