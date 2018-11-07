package com.dm.auth.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

import lombok.Data;

@Data
public class ClientInfoDto implements Serializable {
	private static final long serialVersionUID = 2205256324488223495L;

	private String clientId;

	private String clientSecret;

	private Set<String> scope = Collections.singleton("app");

	private Set<String> authorizedGrantTypes = Collections.singleton("authorization_code");

	private Set<String> registeredRedirectUri;

	private Set<String> resourceIds;

	private Integer accessTokenValiditySeconds;

	private Integer refreshTokenValiditySeconds;

}
