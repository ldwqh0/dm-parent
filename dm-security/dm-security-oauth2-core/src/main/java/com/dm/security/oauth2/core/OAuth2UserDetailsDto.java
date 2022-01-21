package com.dm.security.oauth2.core;

import com.dm.security.core.userdetails.UserDetailsDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.dm.collections.Maps.hashMap;
import static com.dm.collections.Sets.hashSet;
import static java.util.Collections.unmodifiableMap;
import static java.util.Collections.unmodifiableSet;

public class OAuth2UserDetailsDto extends UserDetailsDto implements OAuth2User {

    private static final long serialVersionUID = 1260336499852489771L;

    private final Map<String, Object> attributes;

    private final Set<String> scopes;

    private final String clientId;

    public OAuth2UserDetailsDto(
        String clientId,
        Set<String> scopes,
        Long id,
        String username,
        Collection<? extends GrantedAuthority> grantedAuthority,
        String fullName,
        String mobile,
        String email,
        String regionCode,
        String scenicName,
        Map<String, Object> attributes) {
        super(id, username, null, false, false, false, false, grantedAuthority, fullName, mobile, email, regionCode, scenicName);
        this.clientId = clientId;
        this.scopes = hashSet(scopes);
        this.attributes = hashMap(attributes);
    }

    @Override
    @JsonIgnore
    public String getName() {
        String name = super.getUsername();
        return Objects.isNull(name) ? clientId : name;
    }

    @Override
    @JsonIgnore
    public Map<String, Object> getAttributes() {
        return unmodifiableMap(this.attributes);
    }

    public Set<String> getScopes() {
        return unmodifiableSet(scopes);
    }

    public String getClientId() {
        return clientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OAuth2UserDetailsDto that = (OAuth2UserDetailsDto) o;
        return Objects.equals(attributes, that.attributes) && Objects.equals(scopes, that.scopes) && Objects.equals(clientId, that.clientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), attributes, scopes, clientId);
    }
}
