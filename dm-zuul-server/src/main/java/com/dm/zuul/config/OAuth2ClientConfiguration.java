package com.dm.zuul.config;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.implicit.ImplicitAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.context.request.WebRequest;

import com.dm.oauth2.RoutedOAuth2ClientContext;
import com.dm.security.oauth2.client.filter.RestfulOAuth2ClientContextFilter;
import com.dm.zuul.client.token.grant.code.AddAuthorizationCodeAccessTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OAuth2ClientConfiguration {

    @Autowired
    private AccessTokenRequest accessTokenRequest;

    /**
     * 拦截oauth2错误，以正确的策略执行跳转<br>
     * 这个filter必须注册在Default Security filter之前
     * 
     * @return
     */
    @Bean
    public FilterRegistrationBean<RestfulOAuth2ClientContextFilter> oauth2ClientContextFilter(ObjectMapper om) {
        RestfulOAuth2ClientContextFilter filter = new RestfulOAuth2ClientContextFilter();
        filter.setObjectMapgger(om);
        FilterRegistrationBean<RestfulOAuth2ClientContextFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        // 将这个filter注册到Spring Security Filter之前
        registration.setOrder(-100);
        return registration;
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    protected AccessTokenRequest accessTokenRequest(@Value("#{request.parameterMap}") Map<String, String[]> parameters,
            @Value("#{request.getAttribute('currentUri')}") String currentUri) {
        DefaultAccessTokenRequest request = new DefaultAccessTokenRequest(parameters);
        request.setCurrentUri(currentUri);
        return request;
    }

    @Bean
    public UserInfoRestTemplateCustomizer userInfoRestTemplateCustomizer() {
        AccessTokenProviderChain provicerChain = new AccessTokenProviderChain(Arrays.<AccessTokenProvider>asList(
                new AddAuthorizationCodeAccessTokenProvider(),
                new ImplicitAccessTokenProvider(),
                new ResourceOwnerPasswordAccessTokenProvider(),
                new ClientCredentialsAccessTokenProvider()));

        return new UserInfoRestTemplateCustomizer() {
            @Override
            public void customize(OAuth2RestTemplate template) {
                template.setAccessTokenProvider(provicerChain);
            }
        };
    }

    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
    public OAuth2ClientContext sessionScopeOauth2ClientContext() {
        return new DefaultOAuth2ClientContext(accessTokenRequest);
    }

    /**
     * 定义一个请求级别的OAuth2ClientContext
     * 
     * @return
     */
    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    public OAuth2ClientContext requestScopeOauth2ClientContext() {
        return new DefaultOAuth2ClientContext(accessTokenRequest);
    }

    @Configuration
    @Primary
    static class RoutedOAuth2ClientContextImpl extends RoutedOAuth2ClientContext {

        @Autowired
        private WebRequest request;

        @Autowired
        @Qualifier("sessionScopeOauth2ClientContext")
        private OAuth2ClientContext sessionScopeOauth2ClientContext;

        @Autowired
        @Qualifier("requestScopeOauth2ClientContext")
        private OAuth2ClientContext requestScopeOauth2ClientContext;

        @Override
        protected OAuth2ClientContext determineTargetContext() {
            String h = request.getHeader("Authorization");
            String b = request.getParameter(OAuth2AccessToken.ACCESS_TOKEN);
            if (StringUtils.isNotBlank(h) || StringUtils.isNoneBlank(b)) {
                return requestScopeOauth2ClientContext;
            } else {
                return sessionScopeOauth2ClientContext;
            }
        }
    }

}
