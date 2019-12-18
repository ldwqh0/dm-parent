package com.dm.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.dm.security.authorization.ServerHttpRequestReactiveAuthorizationManager;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange().anyExchange().access(serverHttpRequestReactiveAuthorizationManager());
        http.oauth2Login();
        return http.build();
    }

    @Bean
    public ServerHttpRequestReactiveAuthorizationManager serverHttpRequestReactiveAuthorizationManager() {
        return new ServerHttpRequestReactiveAuthorizationManager();
    }
}
