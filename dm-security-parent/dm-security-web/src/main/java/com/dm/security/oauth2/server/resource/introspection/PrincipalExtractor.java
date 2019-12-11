package com.dm.security.oauth2.server.resource.introspection;

import java.util.Map;

import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

@FunctionalInterface
public interface PrincipalExtractor {

    public OAuth2AuthenticatedPrincipal extract(Map<String, Object> map);

}
