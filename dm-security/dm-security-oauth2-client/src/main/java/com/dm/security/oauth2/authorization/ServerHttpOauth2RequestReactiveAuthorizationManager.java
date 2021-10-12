package com.dm.security.oauth2.authorization;

import com.dm.collections.CollectionUtils;
import com.dm.security.authentication.ResourceAuthorityAttribute;
import com.dm.security.oauth2.core.OAuth2UserDetailsDto;
import com.dm.security.web.authorization.ServerHttpRequestReactiveAuthorizationManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * Oauth2授权管理器
 *
 * @author LiDong
 */
public class ServerHttpOauth2RequestReactiveAuthorizationManager extends ServerHttpRequestReactiveAuthorizationManager implements InitializingBean {

    private ServerOAuth2AuthorizedClientRepository authorizedClientRepository = null;

    public void setAuthorizedClientRepository(ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {
        this.authorizedClientRepository = authorizedClientRepository;
    }

    /**
     * 针对oauth2Client的请求，要验证scope
     */
    @Override
    protected Mono<Boolean> additionalValidate(ResourceAuthorityAttribute attribute, Authentication authentication,
                                               AuthorizationContext context) {
        return Mono.defer(() -> {
            if (authentication instanceof OAuth2AuthenticationToken) {
                OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
                OAuth2User principal = token.getPrincipal();
                // 根据用户的scope判断权限
                if (OAuth2UserDetailsDto.class.isAssignableFrom(principal.getClass())) {
                    Set<String> scopes = ((OAuth2UserDetailsDto) principal).getScopes();
                    Set<String> resourceScopes = attribute.getResource().getScopes();
                    return Mono.just(CollectionUtils.isEmpty(resourceScopes) || CollectionUtils.containsAny(scopes, resourceScopes));
                } else {
                    // TODO 验证的方法应该以授权结果中的scope为准，不应该以客户端请求的scope为准
                    // 但作为客户端，如何获取用户授权的scope是个问题
                    return this.authorizedClientRepository.loadAuthorizedClient(
                            token.getAuthorizedClientRegistrationId(),
                            token, context.getExchange())
                        .map(client -> containsScope(client, attribute))
                        .switchIfEmpty(Mono.error(AuthorizedClientNotFoundException::new));
                }
            } else {
                return Mono.just(Boolean.TRUE);
            }
        });
    }


    // 判断资源的scope是否和授权的Scope一致
    private boolean containsScope(OAuth2AuthorizedClient client, ResourceAuthorityAttribute attribute) {
        OAuth2AccessToken accessToken = client.getAccessToken();
        Set<String> resourceScopes = attribute.getResource().getScopes();
        return CollectionUtils.isEmpty(resourceScopes) || CollectionUtils.containsAny(accessToken.getScopes(), resourceScopes);

    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(authorizedClientRepository, "the authorized client repository can not be null");
    }
}
