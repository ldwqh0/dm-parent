package com.dm.security.oauth2.server.resource.introspection;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import com.dm.security.core.userdetails.UserDetailsDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsDtoOAuth2AuthenticatedPrincipal extends UserDetailsDto implements OAuth2AuthenticatedPrincipal {

    private static final long serialVersionUID = 1260336499852489771L;

    @Override
    @JsonIgnore
    public String getName() {
        return getUsername();
    }

    @Override
    @JsonIgnore
    public Map<String, Object> getAttributes() {
        return new HashMap<String, Object>();
    }

}