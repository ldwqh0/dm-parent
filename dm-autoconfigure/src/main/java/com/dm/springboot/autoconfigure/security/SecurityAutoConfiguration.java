package com.dm.springboot.autoconfigure.security;

import com.dm.security.core.userdetails.GrantedAuthorityDto;
import com.dm.security.core.userdetails.UserDetailsDto;
import com.dm.security.web.authentication.LoginFailureHandler;
import com.dm.security.web.authentication.LoginSuccessHandler;
import com.dm.security.web.controller.CurrentUserController;
import com.dm.security.web.controller.CurrentUserReactiveController;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerAdapter;

import javax.servlet.Servlet;
import java.util.Collections;
import java.util.List;

@Configuration
public class SecurityAutoConfiguration {

    @Configuration
    @ConditionalOnClass(name = {"javax.servlet.Servlet", "com.dm.security.core.userdetails.UserDetailsDto"})
    static class CurrentUserConfiguration {
        @Bean
        public CurrentUserController currentUserController() {
            return new CurrentUserController();
        }
    }

    @ConditionalOnClass(name = {
        "reactor.core.publisher.Mono",
        "com.dm.security.core.userdetails.UserDetailsDto"})
    @ConditionalOnBean(type = {
        "org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerAdapter"})
    static class ReactiveCurrentUserConfiguration {
        @Bean
        public CurrentUserReactiveController currentUserReactiveController() {
            return new CurrentUserReactiveController();
        }
    }

    @Configuration
    @ConditionalOnClass({ReactiveAuthenticationManager.class})
    @ConditionalOnBean({RequestMappingHandlerAdapter.class})
    @ConditionalOnMissingBean(type = {
        "org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter"
    })
    @ConditionalOnMissingClass({"javax.servlet.Servlet"})
    static class ReactiveSecurityConfiguration {

        // TODO 这个需要处理,用于单机使用reactive,方法还没有实现
//        @Bean
//        @ConditionalOnMissingBean(ReactiveUserDetailsService.class)
//        public ReactiveUserDetailsService reactiveUserDetailsService() {
//            return new ReactiveUserDetailsServiceImpl();
//        }

        /**
         * 从oauth2服务器获取用户信息的实现
         *
         * @return
         */
        @ConditionalOnClass(name = {
            "org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService",
            "com.dm.security.oauth2.client.userinfo.DmReactiveOAuth2UserService"
        })
        @ConditionalOnMissingBean(ReactiveOAuth2UserService.class)
        @Bean
        public ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> reactiveOAuth2UserService() {
            return new com.dm.security.oauth2.client.userinfo.DmReactiveOAuth2UserService();
        }

    }

    @ConditionalOnClass({Servlet.class, WebSecurityConfigurerAdapter.class})
    @ConditionalOnMissingBean({WebSecurityConfigurerAdapter.class, SecurityWebFilterChain.class})
    @RequiredArgsConstructor
    static class SecurityConfiguration extends WebSecurityConfigurerAdapter {

        private final ObjectMapper om;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().anyRequest().authenticated().and().formLogin();
            // 设置匿名用户的默认分组
            UserDetailsDto ud = new UserDetailsDto();
            List<GrantedAuthority> authorities = Collections
                .singletonList(new GrantedAuthorityDto("内置分组_ROLE_ANONYMOUS"));
            ud.setGrantedAuthority(authorities);
            http.anonymous().authorities(authorities).principal(ud);
            http.formLogin().successHandler(new LoginSuccessHandler(om)).failureHandler(new LoginFailureHandler(om));
            MediaTypeRequestMatcher mtrm = new MediaTypeRequestMatcher(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN);
            mtrm.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
            http.exceptionHandling().defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), mtrm);
        }
    }

}
