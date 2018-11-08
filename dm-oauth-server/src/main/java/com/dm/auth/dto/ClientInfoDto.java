package com.dm.auth.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties({ "clientSecret" })
public class ClientInfoDto implements Serializable {
	private static final long serialVersionUID = 2205256324488223495L;

	private String clientId;

	private String clientSecret;
	
	private String name;

	private Set<String> scope = Collections.singleton("app");

	private Set<String> authorizedGrantTypes = Collections.singleton("authorization_code");

	private Set<String> registeredRedirectUri;

	private Set<String> resourceIds;

	private Integer accessTokenValiditySeconds;

	private Integer refreshTokenValiditySeconds;

}
