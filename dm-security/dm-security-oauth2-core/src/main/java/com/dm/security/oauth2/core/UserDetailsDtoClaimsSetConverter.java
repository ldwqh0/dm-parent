package com.dm.security.oauth2.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException;

import java.util.Map;


public class UserDetailsDtoClaimsSetConverter implements IntrospectorClaimsSetConverter {

    Logger logger = LoggerFactory.getLogger(UserDetailsDtoClaimsSetConverter.class);

    @Override
    public OAuth2AuthenticatedPrincipal convert(Map<String, Object> map) {
        if (isActive(map)) {

        } else {
            this.logger.trace("Did not validate token since it is inactive");
            throw new BadOpaqueTokenException("Provided token isn't active");
        }
        return null;
    }

    boolean isActive(Map<String, Object> result) {
        Object active = result.get("active");
        return Boolean.TRUE.equals(active);
    }
}
