package com.dm.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationContext;

import com.dm.security.authorization.ServerHttpRequestReactiveAuthorizationManager;
import com.dm.security.oauth2.authorization.ServerHttpOauth2RequestReactiveAuthorizationManager;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange().anyExchange().access(reactiveAuthorizationManager());
        http.oauth2Login();
        return http.build();
    }

    @Bean
    public ReactiveAuthorizationManager<AuthorizationContext> reactiveAuthorizationManager() {
        return new ServerHttpOauth2RequestReactiveAuthorizationManager();
    }
}
