package com.dm.zuul.config;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import com.dm.security.access.RequestAuthoritiesAccessDecisionVoter;
import com.dm.security.access.RequestAuthoritiesFilterInvocationSecurityMetadataSource;
import com.dm.security.core.userdetails.UserDetailsDto;
import com.dm.security.oauth2.provider.token.TokenStoreResourceServerTokenServices;
import com.dm.security.oauth2.provider.token.UserDetailsAuthenticationConverter;
import com.dm.security.oauth2.resource.UserDetailsDtoPrincipalExtractor;

@EnableResourceServer
@EnableWebSecurity
public class ResourceConfigure extends ResourceServerConfigurerAdapter {

//	@Autowired
//	private RemoteTokenServices remoteTokenServices;

	@Autowired
	private RedisConnectionFactory connectionFactory;

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
		resources.tokenServices(tokenService());
	}

	/**
	 * ResourceServer直接连接Redis读取token信息<br />
	 * 如果要使用该模式，需要注释掉配置文件中关于security.oauth2相关配置
	 * 
	 * @return
	 */
	@Bean
	public ResourceServerTokenServices tokenService() {
		TokenStoreResourceServerTokenServices tokenService = new TokenStoreResourceServerTokenServices();
		tokenService.setTokenStore(tokenStore());
		return tokenService;
	}

	@Bean
	public TokenStore tokenStore() {
		return new RedisTokenStore(connectionFactory);
	}

//	/**
//	 * 指定解码Token信息的解码器
//	 */
//	@PostConstruct
//	public void config() {
//		remoteTokenServices.setAccessTokenConverter(accessTokenConverter());
//	}

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
//	@Bean
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
//	@Bean
	public PrincipalExtractor principalExtractor() {
		return new UserDetailsDtoPrincipalExtractor();
	}

}
