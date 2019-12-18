package com.dm.security.oauth2.core;

import java.util.Map;

import org.springframework.security.oauth2.core.user.OAuth2User;

@FunctionalInterface
public interface PrincipalExtractor {

    public OAuth2User extract(Map<String, Object> map);

}
