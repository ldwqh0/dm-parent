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

}