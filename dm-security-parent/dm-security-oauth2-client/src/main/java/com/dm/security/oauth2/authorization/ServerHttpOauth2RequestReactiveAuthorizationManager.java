package com.dm.security.oauth2.authorization;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.web.server.authorization.AuthorizationContext;

import com.dm.collections.CollectionUtils;
import com.dm.security.authentication.ResourceAuthorityAttribute;
import com.dm.security.web.authorization.ServerHttpRequestReactiveAuthorizationManager;

import reactor.core.publisher.Mono;

/**
 * Oauth2授权管理器
 * 
 * @author LiDong
 *
 */
public class  ServerHttpOauth2RequestReactiveAuthorizationManager extends ServerHttpRequestReactiveAuthorizationManager {

    private ServerOAuth2AuthorizedClientRepository authorizedClientRepository = null;

    @Autowired
    public void setAuthorizedClientRepository(ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {
        this.authorizedClientRepository = authorizedClientRepository;
    }

    /**
     * 针对oauth2Client的请求，要验证scope
     */
    @Override
    protected Mono<Boolean> additionalValidate(ResourceAuthorityAttribute attribute, Authentication authentication,
            AuthorizationContext context) {
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
            return this.authorizedClientRepository.loadAuthorizedClient(
                    token.getAuthorizedClientRegistrationId(),
                    token, context.getExchange())
                    .map(client -> containsScope(client, attribute));
        } else {
            return Mono.just(Boolean.TRUE);
        }
    }

    // 判断资源的scope是否和授权的Scope一致
    private boolean containsScope(OAuth2AuthorizedClient client, ResourceAuthorityAttribute attribute) {
        OAuth2AccessToken accessToken = client.getAccessToken();
        Set<String> resourceScopes = attribute.getResource().getScopes();
        if (CollectionUtils.isNotEmpty(resourceScopes)) {
            return CollectionUtils.containsAny(accessToken.getScopes(), resourceScopes);
        } else {
            return Boolean.TRUE;
        }
    }
}
