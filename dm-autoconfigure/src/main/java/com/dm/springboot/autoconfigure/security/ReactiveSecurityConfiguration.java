package com.dm.springboot.autoconfigure.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;

@Configuration
@ConditionalOnClass(ReactiveAuthenticationManager.class)
@ConditionalOnMissingBean(type = {"org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter"})
@ConditionalOnMissingClass({ "javax.servlet.Servlet" })
public class ReactiveSecurityConfiguration {

    @Bean
    @ConditionalOnMissingBean(ReactiveUserDetailsService.class)
    public ReactiveUserDetailsService reactiveUserDetailsService() {
        return new ReactiveUserDetailsServiceImpl();
    }

}
