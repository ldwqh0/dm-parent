package com.dm.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.approval.ApprovalStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;

import com.dm.auth.service.UserApprovalService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableAuthorizationServer
@Configuration
public class OAuthServerConfigurer extends AuthorizationServerConfigurerAdapter {

	@Autowired
	@Qualifier("clientInfoService")
	private ClientDetailsService clientDetailsService;

	@Autowired
	private UserApprovalService userApprovalService;

	/**
	 * 这个主要是针对授权服务的配置，也就是针对/oauth/token这个地址的相关配置，比如添加过滤器什么的
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		// 通常情况下,Spring Security获取token的认证模式是基于http basic的,
		// 也就是client的client_id和client_secret是通过http的header或者url模式传递的，
		// 也就是通过http请求头的 Authorization传递，具体的请参考http basic
		// 或者http://client_id:client_secret@server/oauth/token的模式传递的
		// 当启用这个配置之后，server可以从表单参数中获取相应的client_id和client_secret信息
		security.allowFormAuthenticationForClients();
	}

	/**
	 * 这个是针对连接信息的配置
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		// 这一段代码创建基于内存的client连接信息，和基于内存的用户类似
		// clients.inMemory()
		// .withClient("client")
		// .secret("test")
		// .authorizedGrantTypes("authorization_code")
		// .scopes("app", "cas")
		// // 是否启用自动授权，如果用自动授权，则不会弹出要求用户授权的页面
		// .autoApprove(false).autoApprove("cas");
		// 使用数据库配置的client
		log.info("use clientDetailsService" + clientDetailsService);
		clients.withClientDetails(clientDetailsService);
	}

	/**
	 * 这个是针对/oauth/token这个地址的配置
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		super.configure(endpoints);

//		endpoints.accessTokenConverter(tokenConverter()); // 终结点的accessTokenConverter,用于将用户授权信息转化为token
//		endpoints.tokenStore(tokenStore()); // token存储器
		endpoints.userApprovalHandler(userApprovalHandler()); // 用户授权处理逻辑
//		endpoints.accessTokenConverter(accessTokenConverter)
//		endpoints.tokenStore(tokenStore)
		// endpoints.authenticationManager(); // 这个作用待研究
	}

	@Bean
	public UserApprovalHandler userApprovalHandler() {
		ApprovalStoreUserApprovalHandler handler = new ApprovalStoreUserApprovalHandler();
		handler.setApprovalStore(userApprovalService);
		handler.setRequestFactory(requestFactory());
		return handler;
	}

	@Bean
	public OAuth2RequestFactory requestFactory() {
		return new DefaultOAuth2RequestFactory(clientDetailsService);
	}

}
