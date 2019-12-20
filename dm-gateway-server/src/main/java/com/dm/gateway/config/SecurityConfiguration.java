package com.dm.gateway.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.server.DelegatingServerAuthenticationEntryPoint;
import org.springframework.security.web.server.DelegatingServerAuthenticationEntryPoint.DelegateEntry;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.web.server.util.matcher.MediaTypeServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import com.dm.security.authorization.ServerHttpRequestReactiveAuthorizationManager;
import com.dm.security.oauth2.authorization.ServerHttpOauth2RequestReactiveAuthorizationManager;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        MediaTypeServerWebExchangeMatcher mediaMathcer = new MediaTypeServerWebExchangeMatcher(
                MediaType.APPLICATION_JSON);
        mediaMathcer.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
        DelegatingServerAuthenticationEntryPoint delegationEntripoint = new DelegatingServerAuthenticationEntryPoint(
                new DelegateEntry(mediaMathcer,
                        new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED)));
        delegationEntripoint.setDefaultEntryPoint(new RedirectServerAuthenticationEntryPoint("/oauth2/authorization/oauth2"));
        http.authorizeExchange().anyExchange().access(reactiveAuthorizationManager());
        // 重新配置入口点
        http.exceptionHandling().authenticationEntryPoint(delegationEntripoint);
        http.oauth2Login();
        return http.build();
    }

    @Bean
    public ReactiveAuthorizationManager<AuthorizationContext> reactiveAuthorizationManager() {
        return new ServerHttpOauth2RequestReactiveAuthorizationManager();
    }
}
