package com.dm.webflux.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import com.dm.security.authorization.ServerHttpRequestReactiveAuthorizationManager;
import com.dm.security.oauth2.client.userinfo.DmReactiveOAuth2UserService;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange().anyExchange().access(serverHttpRequestReactiveAuthorizationManager());
        http.oauth2Login();
        return http.build();
    }

    @Bean
    public DmReactiveOAuth2UserService reactiveOAuth2UserService() {
        return new DmReactiveOAuth2UserService();
    }

    @Bean
    public ServerHttpRequestReactiveAuthorizationManager serverHttpRequestReactiveAuthorizationManager() {
        return new ServerHttpRequestReactiveAuthorizationManager();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("123456")
                .roles("USER")
                .build();
        return new MapReactiveUserDetailsService(user);
    }
}
