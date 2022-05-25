package com.dm.springboot.autoconfigure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerAdapter;

import java.util.Collections;
import java.util.List;

@Import({
    SecurityAutoConfiguration.WebMvcSecurityConfigurerAdapter.class,
    SecurityAutoConfiguration.DmOAuth2ResourceServerAutoConfiguration.class,
    SecurityAutoConfiguration.ReactiveSecurityConfiguration.class,
    SecurityAutoConfiguration.CurrentAuthorityControllerAutoConfiguration.class
})
public class SecurityAutoConfiguration {

    @Configuration
    @ConditionalOnClass(value = {
        com.dm.security.core.userdetails.UserDetailsDto.class,
        com.dm.security.web.controller.CurrentAuthorityController.class
    })
    static class CurrentAuthorityControllerAutoConfiguration {
        @Bean
        @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
        public com.dm.security.web.controller.CurrentAuthorityController currentAuthorityController() {
            return new com.dm.security.web.controller.CurrentAuthorityController();
        }

        @Bean
        @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
        public com.dm.security.web.controller.CurrentReactiveController currentUserReactiveController() {
            return new com.dm.security.web.controller.CurrentReactiveController();
        }
    }

    @Configuration
    @ConditionalOnClass(name = {"com.dm.security.oauth2.server.resource.introspection.UserInfoOpaqueTokenIntrospector"})
    @ConditionalOnProperty(name = "spring.security.oauth2.resourceserver.opaquetoken.introspection-uri")
    static class DmOAuth2ResourceServerAutoConfiguration {
        @Bean
        @ConditionalOnMissingBean(OpaqueTokenIntrospector.class)
        public OpaqueTokenIntrospector tokenIntrospector(OAuth2ResourceServerProperties properties) {
            String clientId = properties.getOpaquetoken().getClientId();
            String clientSecret = properties.getOpaquetoken().getClientSecret();
            String introspectionUri = properties.getOpaquetoken().getIntrospectionUri();
            return new com.dm.security.oauth2.server.resource.introspection.UserInfoOpaqueTokenIntrospector(
                introspectionUri,
                clientId,
                clientSecret
            );
        }
    }

    @Configuration
    @ConditionalOnClass({ReactiveAuthenticationManager.class})
    @ConditionalOnBean({RequestMappingHandlerAdapter.class})
    @ConditionalOnMissingBean(type = {
        "org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter"
    })
    @ConditionalOnMissingClass({"javax.servlet.Servlet"})
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    static class ReactiveSecurityConfiguration {

        // TODO 这个需要处理,用于单机使用reactive,方法还没有实现
//        @Bean
//        @ConditionalOnMissingBean(ReactiveUserDetailsService.class)
//        public ReactiveUserDetailsService reactiveUserDetailsService() {
//            return new ReactiveUserDetailsServiceImpl();
//        }

        /**
         * 从oauth2服务器获取用户信息的实现
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

    /**
     * 启动默认的安全配置，这个配置覆盖Spring Security的默认配置
     */
    @Configuration
    @ConditionalOnClass(WebSecurityConfigurerAdapter.class)
    @ConditionalOnMissingBean({WebSecurityConfigurerAdapter.class})
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    static class WebMvcSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        private final ObjectMapper objectMapper;

        public WebMvcSecurityConfigurerAdapter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().anyRequest().authenticated().and().formLogin();
            // 设置匿名用户的默认分组
            List<GrantedAuthority> authorities = Collections.singletonList(com.dm.security.core.userdetails.GrantedAuthorityDto.ROLE_ANONYMOUS);
            com.dm.security.core.userdetails.UserDetailsDto ud = com.dm.security.core.userdetails.UserDetailsDto.builder().username("anonymousUser").grantedAuthority(authorities).build();
            http.anonymous().authorities(authorities).principal(ud);
            http.formLogin().successHandler(new com.dm.security.web.authentication.LoginSuccessHandler(objectMapper)).failureHandler(new com.dm.security.web.authentication.LoginFailureHandler(objectMapper));
            MediaTypeRequestMatcher mediaTypeRequestMatcher = new MediaTypeRequestMatcher(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN);
            mediaTypeRequestMatcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
            http.exceptionHandling().defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), mediaTypeRequestMatcher);
        }
    }

}
