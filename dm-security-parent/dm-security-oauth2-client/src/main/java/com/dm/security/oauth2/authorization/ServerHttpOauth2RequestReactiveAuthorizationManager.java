package com.dm.security.oauth2.authorization;

import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.dm.security.authentication.ResourceAuthorityAttribute;
import com.dm.security.authorization.ServerHttpRequestReactiveAuthorizationManager;

public class ServerHttpOauth2RequestReactiveAuthorizationManager extends ServerHttpRequestReactiveAuthorizationManager {

    /**
     * 针对oauth2Client的请求，要验证scope
     */
    @Override
    protected boolean additionalValidate(ResourceAuthorityAttribute attribute, Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
            OAuth2User user = token.getPrincipal();
            OAuth2AccessToken accessToken = user.getAttribute("accessToken");
            Set<String> resourceScopes = attribute.getResource().getScopes();
            if (CollectionUtils.isNotEmpty(resourceScopes)) {
                return CollectionUtils.containsAny(accessToken.getScopes(), resourceScopes);
            }
        }
        return true;
    }
}
