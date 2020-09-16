package com.dm.security.oauth2.core;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.dm.security.core.userdetails.UserDetailsDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class OAuth2UserDetailsDto extends UserDetailsDto implements OAuth2User {

    private static final long serialVersionUID = 1260336499852489771L;

    private Map<String, Object> attributes = Collections.emptyMap();

    private Set<String> scopes;

    private String clientId;

    public OAuth2UserDetailsDto() {
    }

    @Override
    @JsonIgnore
    public String getName() {
        String name = super.getName();
        return Objects.isNull(name) ? clientId : name;
    }

    @Override
    @JsonIgnore
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Set<String> getScopes() {
        return scopes;
    }

    public void setScopes(Set<String> scopes) {
        this.scopes = scopes;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(attributes, clientId, scopes);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        OAuth2UserDetailsDto other = (OAuth2UserDetailsDto) obj;
        return Objects.equals(attributes, other.attributes) && Objects.equals(clientId, other.clientId)
                && Objects.equals(scopes, other.scopes);
    }

}