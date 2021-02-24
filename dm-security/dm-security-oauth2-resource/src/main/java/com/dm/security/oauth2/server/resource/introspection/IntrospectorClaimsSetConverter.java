package com.dm.security.oauth2.server.resource.introspection;

import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.util.Map;

public interface IntrospectorClaimsSetConverter {
    OAuth2AuthenticatedPrincipal convert(Map<String, Object> map);
}
