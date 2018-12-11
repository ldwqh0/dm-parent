package com.dm.zuul.config;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.dm.security.access.RequestAuthoritiesAccessDecisionVoter;
import com.dm.security.access.RequestAuthoritiesFilterInvocationSecurityMetadataSource;
import com.dm.security.oauth2.provider.token.TokenStoreResourceServerTokenServices;

@EnableResourceServer
@EnableWebSecurity
public class ResourceConfigure extends ResourceServerConfigurerAdapter {

	@Autowired
	private RedisConnectionFactory connectionFactory;

	/**
	 * 配置对资源的保护模式
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		// 指定所有的资源都要被保护
		super.configure(http);
		// 增加自定义的资源授权过滤器
		http.addFilterBefore(interceptor(), FilterSecurityInterceptor.class);
		http.requestMatcher(new BearerTokenRequestMatcher());
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
//		resources.tokenServices(tokenStoreResourceServerTokenServices());
	}

	/**
	 * @return
	 */
	public ResourceServerTokenServices tokenStoreResourceServerTokenServices() {
		TokenStoreResourceServerTokenServices tokenService = new TokenStoreResourceServerTokenServices();
		tokenService.setTokenStore(tokenStore());
		return tokenService;
	}

	@Bean
	public TokenStore tokenStore() {
		return new RedisTokenStore(connectionFactory);
	}

	static class BearerTokenRequestMatcher implements RequestMatcher {
		@Override
		public boolean matches(HttpServletRequest request) {
			String authHeader = request.getHeader("Authorization");
			return StringUtils.startsWithIgnoreCase(authHeader, "bearer");
		}

	}

}
