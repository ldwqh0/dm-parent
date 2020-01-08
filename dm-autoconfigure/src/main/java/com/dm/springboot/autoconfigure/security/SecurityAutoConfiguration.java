package com.dm.springboot.autoconfigure.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import com.dm.security.controller.CurrentUserController;
import com.dm.security.controller.CurrentUserReactiveController;

@Import({ ReactiveSecurityConfiguration.class,
        SecurityConfiguration.class })
public class SecurityAutoConfiguration {

    @Bean
    @ConditionalOnClass(name = { "javax.servlet.Servlet", "com.dm.security.core.userdetails.UserDetailsDto" })
    public CurrentUserController currentUserController() {
        return new CurrentUserController();
    }

    @Bean
    @ConditionalOnClass(name = {
            "reactor.core.publisher.Mono",
            "com.dm.security.core.userdetails.UserDetailsDto" })
    @ConditionalOnBean(type = {
            "org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerAdapter" })
    public CurrentUserReactiveController currentUserReactiveController() {
        return new CurrentUserReactiveController();
    }

}
