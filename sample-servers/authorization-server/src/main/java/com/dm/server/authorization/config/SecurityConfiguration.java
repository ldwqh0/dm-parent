package com.dm.server.authorization.config;

import com.dm.server.authorization.oauth2.ServerOpaqueTokenIntrospector;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.xyyh.authorization.core.OAuth2ResourceServerTokenService;

import java.util.Collections;

/**
 *
 * <p>服务器安全配置</p>
 *
 * @author ldwqh0@outlook.com
 */
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
        // 针对rest请求，返回401
        MediaTypeRequestMatcher mediaTypeRequestMatcher = new MediaTypeRequestMatcher(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN);
        mediaTypeRequestMatcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
        http.exceptionHandling().defaultAuthenticationEntryPointFor(new BearerTokenAuthenticationEntryPoint(), mediaTypeRequestMatcher);
        // 这里不处理匿名用户的问题
        // 正常情况下，所有带token的请求都会认为是resource请求，会返回401状态码
        http.oauth2ResourceServer().opaqueToken().introspector(opaqueTokenIntrospector());
    }


    @Bean
    public ServerOpaqueTokenIntrospector opaqueTokenIntrospector() {
        ServerOpaqueTokenIntrospector tokenIntrospector = new ServerOpaqueTokenIntrospector();
        tokenIntrospector.setAccessTokenService(tokenService);
        return tokenIntrospector;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
