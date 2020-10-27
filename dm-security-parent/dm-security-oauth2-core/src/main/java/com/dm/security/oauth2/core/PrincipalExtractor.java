package com.dm.security.oauth2.core;

import java.util.Map;

@FunctionalInterface
public interface PrincipalExtractor {

    OAuth2UserDetailsDto extract(Map<String, Object> map);

}
