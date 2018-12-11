package com.dm.zuul.config;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import com.dm.security.access.RequestAuthoritiesAccessDecisionVoter;
import com.dm.security.access.RequestAuthoritiesFilterInvocationSecurityMetadataSource;
import com.dm.security.oauth2.provider.token.UserDetailsAuthenticationConverter;
import com.dm.security.oauth2.resource.UserDetailsDtoPrincipalExtractor;

@EnableResourceServer
@EnableWebSecurity
public class ResourceConfigure extends ResourceServerConfigurerAdapter {

	@Autowired
	private RemoteTokenServices remoteTokenServices;

	/**
	 * 配置对资源的保护模式
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		// 指定所有的资源都要被保护
		http.authorizeRequests().anyRequest().authenticated();
		// 增加自定义的资源授权过滤器
		http.addFilterBefore(interceptor(), FilterSecurityInterceptor.class);
		http.antMatcher("/**");
	}

	@Bean
	public FilterSecurityInterceptor interceptor() {
		FilterSecurityInterceptor interceptor = new FilterSecurityInterceptor();
		List<AccessDecisionVoter<?>> voters = Collections.singletonList(new RequestAuthoritiesAccessDecisionVoter());
		AccessDecisionManager accessDecisionManager = new AffirmativeBased(voters);
		interceptor.setAccessDecisionManager(accessDecisionManager);
		interceptor.setSecurityMetadataSource(securityMetadataSource());
		return interceptor;
	}

	@Bean
	public FilterInvocationSecurityMetadataSource securityMetadataSource() {
		return new RequestAuthoritiesFilterInvocationSecurityMetadataSource();
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		super.configure(resources);
		// 指定这是一个restful service,不会保存会话状态
		resources.stateless(true);
//		resources.tokenStore(tokenStore)
	}

	/**
	 * 指定解码Token信息的解码器
	 */
	@PostConstruct
	public void config() {
		remoteTokenServices.setAccessTokenConverter(accessTokenConverter());
	}

	@Bean
	public AccessTokenConverter accessTokenConverter() {
		DefaultAccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();
		tokenConverter.setUserTokenConverter(userTokenConverter());
		return tokenConverter;
	}

	/**
	 * 配置一个Converter，使之可以解析token_info中的Pripical
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
	 * 指定一个用户信息解码器，将从服务器获取过来的用户信息解码为本地UserDetails
	 * 
	 * @return
	 */
	@Bean
	public PrincipalExtractor principalExtractor() {
		return new UserDetailsDtoPrincipalExtractor();
	}

}
