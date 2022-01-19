package com.dm.security.oauth2.core;

import com.dm.collections.Maps;
import com.dm.security.core.userdetails.UserDetailsDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

public class OAuth2UserDetailsDto extends UserDetailsDto implements OAuth2User {

    private static final long serialVersionUID = 1260336499852489771L;

    private final Map<String, Object> attributes = new HashMap<>();

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
        this.scopes = scopes;
        if (Maps.isNotEmpty(attributes)) {
            this.attributes.putAll(attributes);
        }
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
        return Collections.unmodifiableMap(this.attributes);
    }

//    public void putAttributes(Map<String, Object> attributes) {
//        this.attributes.putAll(attributes);
//    }
//
//    public void putAttribute(String key, Object value) {
//        if (Objects.isNull(value)) {
//            this.attributes.remove(key);
//        } else {
//            this.attributes.put(key, value);
//        }
//    }

    public Set<String> getScopes() {
        return Collections.unmodifiableSet(scopes);
    }

    public String getClientId() {
        return clientId;
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
