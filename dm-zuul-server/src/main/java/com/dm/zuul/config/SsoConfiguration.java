package com.dm.zuul.config;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2SsoProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerTokenServicesConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.dm.security.core.userdetails.GrantedAuthorityDto;
import com.dm.security.core.userdetails.UserDetailsDto;
import com.dm.security.oauth2.provider.token.UserDetailsAuthenticationConverter;
import com.dm.security.oauth2.resource.UserDetailsDtoPrincipalExtractor;

/**
 * 配置zuul网关服务的单点登录
 * 
 * @author LiDong
 *
 */
@Configuration
@EnableConfigurationProperties(OAuth2SsoProperties.class)
@Import({ ResourceServerTokenServicesConfiguration.class })
public class SsoConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private RemoteTokenServices remoteTokenServices;

    @Autowired
    private OAuth2SsoProperties ssoProperties;

    @Autowired
    private UserInfoRestTemplateFactory restTemplateFactory;

    @Autowired
    private AuthenticationSuccessHandler loginSuccessHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers(
                HttpMethod.GET,
                "/users/current",
                "/menuAuthorities/current",
                "/p/users/current",
                "/p/menuAuthorities/current").authenticated()
                .anyRequest().access("@authorityChecker.check(authentication,request)");
        // 设定匿名用户的用户实体
        UserDetailsDto ud = new UserDetailsDto();
        List<GrantedAuthority> authorities = Collections.singletonList(new GrantedAuthorityDto("内置分组_ROLE_ANONYMOUS"));
        ud.setGrantedAuthority(authorities);
        http.anonymous().authorities(authorities).principal(ud);
        // -------------------
        http.csrf().disable();
        http.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
        http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
        // 此处配置oauth2单点登录配置
        // 这个配置需要在所有的类初始化完成之后， 因为需要设置 setSessionAuthenticationStrategy
        http.apply(new OAuth2ClientAuthenticationConfigurer(oauth2SsoFilter()));
    }

    /**
     * 如果使用/oauth/check_token 端点解析用户信息，需要额外的转换来处理{@link UserDetails}信息
     * 
     * @return
     */
    @Bean
    public AccessTokenConverter accessTokenConverter() {
        DefaultAccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();
        tokenConverter.setUserTokenConverter(userTokenConverter());
        return tokenConverter;
    }

    /**
     * 配置一个Converter，使之可以解析token_info中的Pripical<br />
     * 此处将 {@link Principal}转换为一个 {@link UserDetailsDto}对象
     * 
     * @return
     */
    @Bean
    public UserAuthenticationConverter userTokenConverter() {
        UserDetailsAuthenticationConverter authenticationConverter = new UserDetailsAuthenticationConverter();
        authenticationConverter.setPrincipalExtractor(principalExtractor());
        return authenticationConverter;
    }

    /**
     * 指定一个用户信息解码器，将从服务器获取过来的用户信息解码为本地{@link UserDetailsDto}
     * 
     * @return
     */
    @Bean
    public PrincipalExtractor principalExtractor() {
        return new UserDetailsDtoPrincipalExtractor();
    }

    private OAuth2ClientAuthenticationProcessingFilter oauth2SsoFilter() {
        OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(
                ssoProperties.getLoginPath());
        filter.setRestTemplate(restTemplateFactory.getUserInfoRestTemplate());
        filter.setTokenServices(remoteTokenServices);
        filter.setApplicationEventPublisher(getApplicationContext());
        filter.setAuthenticationSuccessHandler(loginSuccessHandler);
        return filter;
    }

    /**
     * 指定解码Token信息的解码器
     */
    @PostConstruct
    public void config() {
        remoteTokenServices.setAccessTokenConverter(accessTokenConverter());
    }

    private static class OAuth2ClientAuthenticationConfigurer
            extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
        private OAuth2ClientAuthenticationProcessingFilter filter;

        OAuth2ClientAuthenticationConfigurer(
                OAuth2ClientAuthenticationProcessingFilter filter) {
            this.filter = filter;
        }

        @Override
        public void configure(HttpSecurity builder) throws Exception {
            OAuth2ClientAuthenticationProcessingFilter ssoFilter = this.filter;
            ssoFilter.setSessionAuthenticationStrategy(builder.getSharedObject(SessionAuthenticationStrategy.class));
            builder.addFilterAfter(ssoFilter, AbstractPreAuthenticatedProcessingFilter.class);
        }
    }
}
