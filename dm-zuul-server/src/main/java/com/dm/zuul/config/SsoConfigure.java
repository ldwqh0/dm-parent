package com.dm.zuul.config;

import java.security.Principal;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

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
@EnableOAuth2Sso
@EnableWebSecurity
public class SsoConfigure extends WebSecurityConfigurerAdapter {

	@Autowired
	private FilterSecurityInterceptor filterSecurityInterceptor;

	@Autowired
	private RemoteTokenServices remoteTokenServices;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		// 指定相关资源的权限校验过滤器
		http.addFilterBefore(filterSecurityInterceptor, FilterSecurityInterceptor.class);
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

	/**
	 * 指定解码Token信息的解码器
	 */
	@PostConstruct
	public void config() {
		remoteTokenServices.setAccessTokenConverter(accessTokenConverter());
	}
}
