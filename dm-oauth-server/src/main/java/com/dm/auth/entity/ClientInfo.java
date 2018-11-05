package com.dm.auth.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Persistable;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "dm_client_info_")
@Getter
@Setter
public class ClientInfo implements Persistable<String>, Serializable {
	private static final long serialVersionUID = -8180613850135404512L;

	@Id
	@Column(name = "client_id_")
	private String id;

	@Column(name = "client_secret_")
	private String clientSecret;

	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> scope;

	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> authorizedGrantTypes;

	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> registeredRedirectUri;

	@Column(name = "access_token_validity_seconds_")
	private Integer accessTokenValiditySeconds;

	@Column(name = "refresh_token_validity_seconds_")
	private Integer refreshTokenValiditySeconds;

	@Column(name = "auto_approve_", nullable = false)
	private boolean autoApprove = false;

	@Override
	public boolean isNew() {
		return StringUtils.isBlank(id);
	}
}
