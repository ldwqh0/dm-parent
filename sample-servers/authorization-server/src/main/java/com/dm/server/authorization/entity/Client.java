package com.dm.server.authorization.entity;

import com.dm.collections.Maps;
import com.dm.common.entity.Audit;
import com.dm.common.entity.CreateAudit;
import com.dm.common.entity.ModifyAudit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.dm.collections.Sets.hashSet;
import static java.util.Collections.unmodifiableMap;
import static java.util.Collections.unmodifiableSet;

/**
 * <p>Client</p>
 *
 * @author ldwqh0@outlook.com
 */
@Getter
@Setter
@Entity
@Table(name = "dm_client_", uniqueConstraints = {
    @UniqueConstraint(name = "UK_dm_client_name_", columnNames = "name_")
})
@EntityListeners(AuditingEntityListener.class)
public class Client implements Auditable<Audit, String, ZonedDateTime> {

    private static final long serialVersionUID = -5684160157895723536L;

    @Id
    @Setter(AccessLevel.NONE)
    @Column(name = "id_", length = 36)
    private String id;

    @Column(name = "name_", length = 100, nullable = false, unique = true)
    private String name;

    @Column(name = "secret_", nullable = false)
    private String secret;

    private CreateAudit createdBy;

    private ModifyAudit lastModifiedBy;

    private ZonedDateTime createdDate;

    private ZonedDateTime lastModifiedDate;

    /**
     * token失效时间
     */
    @Column(name = "access_token_validity_seconds_")
    private Integer accessTokenValiditySeconds;

    /**
     * refreshToken失效时间
     */
    @Column(name = "refresh_token_validity_seconds_")
    private Integer refreshTokenValiditySeconds;

    @ElementCollection
    @Column(name = "authorized_grant_type_", nullable = false)
    @CollectionTable(name = "dm_client_authorized_grant_type_", joinColumns = {
        @JoinColumn(name = "client_id_", foreignKey = @ForeignKey(name = "FK_dm_client_authorized_grant_type_client_id_"))
    })
    private Set<String> authorizedGrantTypes;


    @ElementCollection
    @CollectionTable(name = "dm_client_scope_", joinColumns = {
        @JoinColumn(name = "client_id_", foreignKey = @ForeignKey(name = "FK_dm_client_scope_client_id_"))
    })
    @Column(name = "scope_", nullable = false)
    private Set<String> scopes;


    @Setter(AccessLevel.NONE)
    @ElementCollection
    @JoinTable(name = "dm_client_additional_information_", uniqueConstraints = {
        @UniqueConstraint(name = "UK_dm_client_additional_information_client_id_key_value_", columnNames = {"client_id_", "key_", "value_"})
    }, joinColumns = {
        @JoinColumn(name = "client_id_", foreignKey = @ForeignKey(name = "FK_dm_client_additional_information_client_id_"))
    })
    @MapKeyColumn(name = "key_")
    @Column(name = "value_", nullable = false)
    private Map<String, String> additionalInformation = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "dm_client_registered_redirect_uri_", joinColumns = {
        @JoinColumn(name = "client_id_", foreignKey = @ForeignKey(name = "FK_dm_client_registered_redirect_uri_client_id_"))
    })
    @Column(name = "registered_redirect_uri_", nullable = false)
    private Set<String> registeredRedirectUris;


    @ElementCollection
    @CollectionTable(name = "dm_client_authority_", joinColumns = {
        @JoinColumn(name = "client_id_", foreignKey = @ForeignKey(name = "FK_dm_client_authority_client_id_"))
    })
    @Column(name = "authority_", nullable = false)
    private Set<String> authorities;


    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "resource_id_", nullable = false)
    @CollectionTable(name = "dm_client_resource_id_", joinColumns = {
        @JoinColumn(name = "client_id_", foreignKey = @ForeignKey(name = "FK_dm_client_resource_id_client_id_"))
    })
    private Set<String> resourceIds;


    @Column(name = "auto_approve_", nullable = false)
    private boolean autoApprove = false;

    @Column(name = "require_pkce_", nullable = false)
    private boolean requirePkce = false;

    public Client() {
        this.id = UUID.randomUUID().toString();
    }

    public Client(String id) {
        this.id = id;
    }

    @Override
    public boolean isNew() {
        return Objects.isNull(id);
    }

    @Override
    public Optional<Audit> getCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    @Override
    public void setCreatedBy(Audit createdBy) {
        this.createdBy = new CreateAudit(createdBy);
    }

    @Override
    public Optional<ZonedDateTime> getCreatedDate() {
        return Optional.ofNullable(createdDate);
    }


    @Override
    public Optional<Audit> getLastModifiedBy() {
        return Optional.ofNullable(lastModifiedBy);
    }

    @Override
    public void setLastModifiedBy(Audit lastModifiedBy) {
        this.lastModifiedBy = new ModifyAudit(lastModifiedBy);
    }

    @Override
    public Optional<ZonedDateTime> getLastModifiedDate() {
        return Optional.of(lastModifiedDate);
    }

    public void setScopes(Set<String> scopes) {
        this.scopes = hashSet(scopes);
    }

    public Set<String> getScopes() {
        return unmodifiableSet(scopes);
    }

    public Set<String> getRegisteredRedirectUris() {
        return unmodifiableSet(registeredRedirectUris);
    }

    public void setAuthorizedGrantTypes(Set<String> authorizedGrantTypes) {
        this.authorizedGrantTypes = hashSet(authorizedGrantTypes);
    }

    public Set<String> getAuthorizedGrantTypes() {
        return unmodifiableSet(this.authorizedGrantTypes);
    }


    public void setRegisteredRedirectUris(Set<String> registeredRedirectUri) {
        this.registeredRedirectUris = hashSet(registeredRedirectUri);
    }

    public Set<String> getAuthorities() {
        return unmodifiableSet(authorities);
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = hashSet(authorities);
    }

    public Set<String> getResourceIds() {
        return unmodifiableSet(resourceIds);
    }

    public void setResourceIds(Set<String> resourceIds) {
        this.resourceIds = hashSet(resourceIds);
    }

    public boolean isRequirePkce() {
        return requirePkce;
    }

    public void setRequirePkce(boolean requirePkce) {
        this.requirePkce = requirePkce;
    }

    public synchronized void additionalInformation(String key, String value) {
        this.additionalInformation.put(key, value);
    }

    public Map<String, String> getAdditionalInformation() {
        return unmodifiableMap(this.additionalInformation);
    }

    public String getAdditionalInformation(String key) {
        if (Maps.isEmpty(this.additionalInformation)) {
            return null;
        } else {
            return this.additionalInformation.get(key);
        }
    }
}
