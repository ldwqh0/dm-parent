package com.dm.server.authorization.config;

import com.dm.server.authorization.oauth2.ServerOpaqueTokenIntrospector;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.xyyh.authorization.core.OAuth2ResourceServerTokenService;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final OAuth2ResourceServerTokenService tokenService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/oauth2/styles/**", "/oauth2/images/**", "/error*").permitAll();
        http.authorizeRequests()
            .anyRequest().authenticated()
            .and().formLogin().loginPage("/oauth2/login.html").loginProcessingUrl("/oauth2/login").permitAll()
            .and().httpBasic().disable();
        http.oauth2ResourceServer().opaqueToken().introspector(opaqueTokenIntrospector());
    }

    @Bean
    public ServerOpaqueTokenIntrospector opaqueTokenIntrospector() {
        ServerOpaqueTokenIntrospector tokenIntrospector = new ServerOpaqueTokenIntrospector();
        tokenIntrospector.setAccessTokenService(tokenService);
        return tokenIntrospector;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
