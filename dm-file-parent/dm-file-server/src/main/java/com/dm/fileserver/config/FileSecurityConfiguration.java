package com.dm.fileserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import com.dm.security.oauth2.server.resource.introspection.UserInfoOpaqueTokenIntrospector;

@EnableWebSecurity
public class FileSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private OAuth2ResourceServerProperties properties;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET).permitAll()
                .anyRequest().authenticated();
        http.oauth2ResourceServer()
                .opaqueToken().introspector(opaqueTokenIntrospector());
    }

    @Bean
    public OpaqueTokenIntrospector opaqueTokenIntrospector() {
        return new UserInfoOpaqueTokenIntrospector(properties.getOpaquetoken().getIntrospectionUri());
    }

}