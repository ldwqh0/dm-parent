package com.dm.security.oauth2.support.entity;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dm.common.entity.Audit;
import com.dm.common.entity.CreateAudit;
import com.dm.common.entity.ModifyAudit;

@Entity
@Table(name = "dm_hdc_client_")
@EntityListeners(AuditingEntityListener.class)
public class Client implements Auditable<Audit, UUID, ZonedDateTime>{

    @Id
    @Column(name = "id_", length = 36)
    @GeneratedValue(generator = "ordered-uuid")
    @Type(type = "uuid-char")
    @GenericGenerator(name = "ordered-uuid", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
            @Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")
    })
    private UUID id;

    @Column(name = "name_", length = 100)
    private String name;

    @Column(name = "secret_")
    private String secret;

    private CreateAudit createdBy;

    private ModifyAudit lastedModifyBy;

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
    @Column(name = "authorized_grant_type_")
    @JoinColumn(name = "client_id_")
    @CollectionTable(name = "dm_hdc_client_authorized_grant_type_")
    private Set<String> authorizedGrantTypes;

    @ElementCollection
    @CollectionTable(name = "dm_hdc_client_scope_")
    @JoinColumn(name = "client_id_")
    @Column(name = "scope_")
    private Set<String> scopes;

    @ElementCollection
    @JoinTable(name = "dm_hdc_client_additional_information_")
    @MapKeyJoinColumn(name = "client_id_")
    @MapKeyColumn(name = "key_")
    @Column(name = "value_")
    private Map<String, String> additionalInformation;

    @ElementCollection
    @JoinColumn(name = "client_id_")
    @CollectionTable(name = "dm_hdc_client_registered_redirect_uri_")
    @Column(name = "registered_redirect_uri_")
    private Set<String> registeredRedirectUri;

    public Integer getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    public void setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    void setId(UUID id) {
        this.id = id;
    }

    @Override
    public UUID getId() {
        return id;
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
        return Optional.<ZonedDateTime>of(createdDate);
    }

    @Override
    public void setCreatedDate(ZonedDateTime creationDate) {
        this.createdDate = creationDate;
    }

    @Override
    public Optional<Audit> getLastModifiedBy() {
        return Optional.<Audit>ofNullable(lastedModifyBy);
    }

    @Override
    public void setLastModifiedBy(Audit lastModifiedBy) {
        this.lastedModifyBy = new ModifyAudit(lastModifiedBy);
    }

    @Override
    public Optional<ZonedDateTime> getLastModifiedDate() {
        return Optional.<ZonedDateTime>of(lastModifiedDate);
    }

    @Override
    public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Collection<String> getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    public void setAuthorizedGrantTypes(Set<String> authorizedGrantTypes) {
        this.authorizedGrantTypes = authorizedGrantTypes;
    }

    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    public Set<String> getRegisteredRedirectUri() {
        return registeredRedirectUri;
    }

    public void setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    }

    public void setScopes(Set<String> scopes) {
        this.scopes = scopes;
    }

    public Collection<String> getScopes() {
        return scopes;
    }

    public Map<String, String> getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(Map<String, String> additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public void setRegisteredRedirectUri(Set<String> registeredRedirectUri) {
        this.registeredRedirectUri = registeredRedirectUri;
    }
}
