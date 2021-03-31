package com.dm.security.oauth2.client.authentication;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthorizationCodeAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>一个代理的Oauth2登录验证器 </p>
 * <p>当系统中存在多个oauth2 login登录方式时，需要一种模式来确定不用的登录客户端验证用户的方式</p>
 * <p>这里通过提供代理认证器的模式，针对不同的的oauth2认证服务客户端，进行不用的oauth2 login认证模式</p>
 */
public class DelegatingOAuth2LoginReactiveAuthenticationManager implements
    ReactiveAuthenticationManager {
    private final ReactiveAuthenticationManager defaultAuthenticationManager;

    private final ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> defaultUserService;

    private final Map<String, ReactiveAuthenticationManager> authenticationManagerDelegates = new HashMap<>();

    private final Map<String, ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User>> userServiceDelegates = new HashMap<>();
    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    public DelegatingOAuth2LoginReactiveAuthenticationManager(
        ReactiveAuthenticationManager defaultAuthenticationManager,
        ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> defaultUserService) {
        Assert.notNull(defaultAuthenticationManager, "defaultAuthenticationManager cannot be null");
        Assert.notNull(defaultUserService, "userService cannot be null");
        this.defaultAuthenticationManager = defaultAuthenticationManager;
        this.defaultUserService = defaultUserService;
    }

    public void put(String key, ReactiveAuthenticationManager authenticationManager, ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> userDetailsService) {
        this.authenticationManagerDelegates.put(key, authenticationManager);
        this.userServiceDelegates.put(key, userDetailsService);
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.defer(() -> {
            OAuth2AuthorizationCodeAuthenticationToken token = (OAuth2AuthorizationCodeAuthenticationToken) authentication;

            // Section 3.1.2.1 Authentication Request - https://openid.net/specs/openid-connect-core-1_0.html#AuthRequest
            // scope REQUIRED. OpenID Connect requests MUST contain the "openid" scope value.
            if (token.getAuthorizationExchange()
                .getAuthorizationRequest().getScopes().contains("openid")) {
                // This is an OpenID Connect Authentication Request so return null
                // and let OidcAuthorizationCodeReactiveAuthenticationManager handle it instead once one is created
                return Mono.empty();
            }

            String registrationId = token.getClientRegistration().getRegistrationId();
            ReactiveAuthenticationManager resolved = authenticationManagerDelegates.getOrDefault(registrationId, defaultAuthenticationManager);
            return resolved.authenticate(token)
                .onErrorMap(OAuth2AuthorizationException.class, e -> new OAuth2AuthenticationException(e.getError(), e.getError().toString()))
                .cast(OAuth2AuthorizationCodeAuthenticationToken.class)
                .flatMap(r -> this.onSuccess(r, registrationId));
        });
    }

    private Mono<OAuth2LoginAuthenticationToken> onSuccess(OAuth2AuthorizationCodeAuthenticationToken authentication, String registrationId) {
        OAuth2AccessToken accessToken = authentication.getAccessToken();
        Map<String, Object> additionalParameters = authentication.getAdditionalParameters();
        OAuth2UserRequest userRequest = new OAuth2UserRequest(authentication.getClientRegistration(), accessToken, additionalParameters);

        ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> resolvedUserService = userServiceDelegates.getOrDefault(registrationId, defaultUserService);

        return resolvedUserService.loadUser(userRequest)
            .map(oauth2User -> {
                Collection<? extends GrantedAuthority> mappedAuthorities =
                    this.authoritiesMapper.mapAuthorities(oauth2User.getAuthorities());
                return new OAuth2LoginAuthenticationToken(
                    authentication.getClientRegistration(),
                    authentication.getAuthorizationExchange(),
                    oauth2User,
                    mappedAuthorities,
                    accessToken,
                    authentication.getRefreshToken());
            });
    }
}
