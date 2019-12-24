package com.dm.security.oauth2.core;

import java.util.Map;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.dm.security.core.userdetails.UserDetailsDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsDtoOAuth2User extends UserDetailsDto implements OAuth2User {

    private static final long serialVersionUID = 1260336499852489771L;

    private Map<String, Object> attributes;

    @Override
    @JsonIgnore
    public String getName() {
        return getUsername();
    }

    @Override
    @JsonIgnore
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
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
        UserDetailsDtoOAuth2User other = (UserDetailsDtoOAuth2User) obj;
        if (attributes == null) {
            if (other.attributes != null)
                return false;
        } else if (!attributes.equals(other.attributes))
            return false;
        return true;
    }

}