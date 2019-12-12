package com.dm.fileserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
//@Configuration
public class ResourceServerConfigurer {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange().anyExchange().authenticated()
                .and().oauth2ResourceServer().bearerTokenConverter(exchange -> {
                    var s = exchange.getPrincipal();
                    s.map(p -> {
                        System.out.println(p);
                        return p;
                    });
                    return null;
                }).opaqueToken();
//                .httpBasic(Customizer.withDefaults())
//                .formLogin(Customizer.withDefaults());
        return http.build();
    }
}
