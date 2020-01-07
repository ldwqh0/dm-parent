package com.dm.auth.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Persistable;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "dm_client_")
public class ClientInfo implements Persistable<String>, Serializable {
    private static final long serialVersionUID = -8180613850135404512L;

    @Id
    @Column(name = "client_id_", nullable = false, length = 50)
    private String clientId;

    @Column(name = "client_secret_")
    private String clientSecret;

    @Column(name = "name_")
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "scope_")
    @CollectionTable(name = "dm_client_scope_", joinColumns = {
            @JoinColumn(name = "client_")
    })
    private Set<String> scope;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "authorized_grant_type_")
    @CollectionTable(name = "dm_client_authorized_grant_type_", joinColumns = {
            @JoinColumn(name = "client_")
    })
    private Set<String> authorizedGrantTypes;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "registered_redirect_uri_")
    @CollectionTable(name = "dm_client_registered_redirect_uri_", joinColumns = {
            @JoinColumn(name = "client_")
    })
    private Set<String> registeredRedirectUri;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "resource_id_")
    @CollectionTable(name = "dm_client_resource_id_", joinColumns = {
            @JoinColumn(name = "client_")
    })
    private Set<String> resourceIds;

    @Column(name = "access_token_validity_seconds_")
    private Integer accessTokenValiditySeconds;

    @Column(name = "refresh_token_validity_seconds_")
    private Integer refreshTokenValiditySeconds;

    @Column(name = "auto_approve_", nullable = false)
    private boolean autoApprove = false;

    @Override
    public boolean isNew() {
        return StringUtils.isBlank(clientId);
    }

    @Override
    public String getId() {
        return this.clientId;
    }

}
