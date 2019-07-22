package com.dm.zuul.config;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.implicit.ImplicitAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.RedirectStrategy;

import com.dm.zuul.client.token.grant.code.AddAuthorizationCodeAccessTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableOAuth2Client
public class OAuth2ClientConfiguration {

	@Autowired
	private ObjectMapper objectMapper;

	@Resource
	@Qualifier("accessTokenRequest")
	private AccessTokenRequest accessTokenRequest;

	@Autowired
	private OAuth2ClientContextFilter oAuth2ClientContextFilter;

	@PostConstruct
	public void setFilter() {
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

	@Bean
	public UserInfoRestTemplateCustomizer userInfoRestTemplateCustomizer() {
		AccessTokenProviderChain provicerChain = new AccessTokenProviderChain(Arrays.<AccessTokenProvider>asList(
				// 更换原有的AuthorizationCodeAccessTokenProvider
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
	@Primary
	@Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
	public OAuth2ClientContext oauth2ClientContext() {
		return new DefaultOAuth2ClientContext(accessTokenRequest);
	}

//	@Bean
//	@Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
//	public OAuth2ClientContext sessionScopeOauth2ClientContext() {
//		return new DefaultOAuth2ClientContext(accessTokenRequest);
//	}

//	@Bean
//	@Primary
//	public OAuth2ClientContext oauth2ClientContext() {
//		return new RoutedOAuth2ClientContext() {
//			@Override
//			protected OAuth2ClientContext determineTargetContext() {
//				return sessionScopeOauth2ClientContext();
//			}
//		};
//	}

}
