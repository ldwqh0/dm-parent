package com.dm.auth.dto;

import java.time.ZonedDateTime;
import java.util.Set;

import lombok.Data;

@Data
public class AccessTokenInfoDto {

	private String username;
	private Set<String> scope;
	private ZonedDateTime expiration;
	private String value;
	private String tokenType;

}
