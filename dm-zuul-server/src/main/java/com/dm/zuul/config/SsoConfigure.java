package com.dm.zuul.config;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;

import com.dm.security.core.userdetails.UserDetailsDto;
import com.dm.security.oauth2.provider.token.UserDetailsAuthenticationConverter;
import com.dm.security.oauth2.resource.UserDetailsDtoPrincipalExtractor;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 配置zuul网关服务的单点登录
 * 
 * @author LiDong
 *
 */
@Configuration
@EnableOAuth2Sso
public class SsoConfigure extends WebSecurityConfigurerAdapter implements ApplicationContextAware {

	@Autowired
	private FilterSecurityInterceptor filterSecurityInterceptor;

	@Autowired
	private RemoteTokenServices remoteTokenServices;

	@Autowired
	private OAuth2ClientContextFilter oAuth2ClientContextFilter;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		// 指定相关资源的权限校验过滤器
		http.addFilterBefore(filterSecurityInterceptor, FilterSecurityInterceptor.class);
		http.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
		http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
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
		oAuth2ClientContextFilter.setRedirectStrategy(new RedirectStrategy() {
			@Override
			public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url)
					throws IOException {
				// 重新配置oauth2Context的重定向策略，不进行重定向，而是将重定向信息交由前端处理
				response.setStatus(HttpStatus.SEE_OTHER.value());
				TreeMap<String, String> parameterMap = new TreeMap<>();
				parameterMap.put("redirect_url", url);
				try (PrintWriter writer = response.getWriter()) {
					writer.write(objectMapper.writeValueAsString(parameterMap));
				}
			}
		});
	}
}
