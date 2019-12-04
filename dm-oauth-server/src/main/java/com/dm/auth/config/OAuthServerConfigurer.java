package com.dm.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
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
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import com.dm.auth.provider.endpoint.DmOauthRedirectResolver;
import com.dm.auth.service.UserApprovalService;
import com.dm.security.oauth2.provider.token.UserDetailsAuthenticationConverter;

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

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

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
		// 默认情况下，checkToken的验证是denyAll的，需要手动开启
		security.checkTokenAccess("isAuthenticated()");
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
		log.info("use clientDetailsService {}", clientDetailsService.toString());
		clients.withClientDetails(clientDetailsService);
	}

	/**
	 * 这个是针对/oauth/token这个地址的配置
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		super.configure(endpoints);
		endpoints.userApprovalHandler(userApprovalHandler()); // 用户授权处理逻辑
		// 如果使用自定义的tokenService,以下的配置都不可用，需要在tokenService中重新配置
		endpoints.tokenStore(tokenStore());
		endpoints.accessTokenConverter(accessTokenConverter());
		// 指定是否可以重用refreshToken
		endpoints.reuseRefreshTokens(false);
		// 如果要使用RefreshToken可用，必须指定UserDetailsService
		endpoints.userDetailsService(userDetailsService);
		// 指定某个连接的redirectResolver
		endpoints.redirectResolver(new DmOauthRedirectResolver());
		// 使用这个authenticationManager可以启用密码模式
		endpoints.authenticationManager(authenticationManager);
	}

	@Bean
	public AccessTokenConverter accessTokenConverter() {
		DefaultAccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();
		tokenConverter.setUserTokenConverter(userTokenConverter());
		return tokenConverter;
	}

	@Bean
	public UserAuthenticationConverter userTokenConverter() {
		UserDetailsAuthenticationConverter userTokenConverter = new UserDetailsAuthenticationConverter();
		return userTokenConverter;
	}

	@Bean
	public UserApprovalHandler userApprovalHandler() {
		// 存储用户的授权结果
		ApprovalStoreUserApprovalHandler handler = new ApprovalStoreUserApprovalHandler();
		handler.setApprovalStore(userApprovalService);
		handler.setRequestFactory(requestFactory());
		return handler;
	}

	@Bean
	public OAuth2RequestFactory requestFactory() {
		return new DefaultOAuth2RequestFactory(clientDetailsService);
	}

	// 如果使用redis来配置tokenStore,需要启动这个bean
//    @Bean
//    public TokenStore tokenStore() {
//        RedisTokenStore tokenStore = new RedisTokenStore(connectionFactory);
//        return tokenStore;
//    }

	// 项目中用到了token管理，需要直接操作tokensotre
	// 而默认的oauth2配置中，tokenStore不是一个bean,不能被我们的代码获取到
	// 因此这里重新配置一个bean,以便让我们的应用可用控制tokenStore
	@Bean
	public TokenStore tokenStore() {
		return new InMemoryTokenStore();
	}

}
