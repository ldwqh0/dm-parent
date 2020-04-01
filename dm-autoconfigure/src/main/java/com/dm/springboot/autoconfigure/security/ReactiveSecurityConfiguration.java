package com.dm.springboot.autoconfigure.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerAdapter;

@Deprecated
@Configuration
@ConditionalOnClass({ ReactiveAuthenticationManager.class })
@ConditionalOnBean({ RequestMappingHandlerAdapter.class })
@ConditionalOnMissingBean(type = {
        "org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter" })
@ConditionalOnMissingClass({ "javax.servlet.Servlet" })
public class ReactiveSecurityConfiguration {

    // TODO 这个需要处理
    @Bean
    @ConditionalOnMissingBean(ReactiveUserDetailsService.class)
    public ReactiveUserDetailsService reactiveUserDetailsService() {
        return new ReactiveUserDetailsServiceImpl();
    }

    @ConditionalOnClass({ ReactiveOAuth2UserService.class,
            com.dm.security.oauth2.client.userinfo.DmReactiveOAuth2UserService.class })
    @ConditionalOnMissingBean(ReactiveOAuth2UserService.class)
    @Bean
    public ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> reactiveOAuth2UserService() {
        return new com.dm.security.oauth2.client.userinfo.DmReactiveOAuth2UserService();
    }

}
