package com.dm.zuul.config;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.dm.security.access.RequestAuthoritiesAccessDecisionVoter;
import com.dm.security.access.RequestAuthoritiesFilterInvocationSecurityMetadataSource;

/**
 * 当请求中携带了access_token之后， 进行oauth认证
 * 
 * @author LiDong
 *
 */
@EnableResourceServer
@EnableWebSecurity
@Order(99)
public class ResourceConfigure extends ResourceServerConfigurerAdapter {

	/**
	 * 配置对资源的保护模式
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		// 指定所有的资源都要被保护
		super.configure(http);
		// TODO 这里待处理优化
		http.authorizeRequests().accessDecisionManager(accessDecisionManager());
		http.authorizeRequests().withObjectPostProcessor(
				new ObjectPostProcessor<FilterSecurityInterceptor>() {
					@Override
					public <O extends FilterSecurityInterceptor> O postProcess(O interceptor) {
						interceptor.setSecurityMetadataSource(securityMetadataSource());
						return interceptor;
					}
				});
		// 仅仅将携带了token的资源，定义为资源服务器的资源，走oauth认证
		// ，其它的资源都走普通的session认证
		http.requestMatcher(new BearerTokenRequestMatcher());

		// 需要重新配置匿名认证的模式，匿名认证的时候，返回一个默认的匿名用户？
		// 资源服务器要如何处理匿名访问？是单独配置？还是通过token的方式传递用户信息到上游服务器
		// 决定 ，网关暂时不做匿名的处理
		// 暂时不做匿名处理
	}

	@Bean
	public AccessDecisionManager accessDecisionManager() {
		List<AccessDecisionVoter<?>> voters = new ArrayList<>(2);
		voters.add(new RequestAuthoritiesAccessDecisionVoter());
		// 使用oauth2认证的时候，会默认的生成一条Webexpression,必须让 accessDecisionManager中的投票器能处理相关规则
		// 否则在启动的时候会报错 参见 AccessDecisionManager.supports
		// 但在实际的授权过程中，FilterInvocationSecurityMetadataSource已经被替换了，事实上不需要处理WebExpression了
		voters.add(new WebExpressionVoter());
		AccessDecisionManager accessDecisionManager = new AffirmativeBased(voters);
		return accessDecisionManager;
	}

	/**
	 * 定义一个FilterInvocationSecurityMetadataSource <br>
	 * 
	 * 可以从数据库读取授权信息
	 * 
	 * @return
	 */
	@Bean
	public FilterInvocationSecurityMetadataSource securityMetadataSource() {
		return new RequestAuthoritiesFilterInvocationSecurityMetadataSource();
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		super.configure(resources);
		// 指定这是一个restful service,不会保存会话状态
		resources.resourceId("ZUUL");
		resources.stateless(true);

		// 这里指定通过token store来校验token
		// 当第三方服务通过access_token来访问服务时，直接从token_store中获取相关信息，而不用再发起远程调用请求
		// resources.tokenServices(tokenStoreResourceServerTokenServices());
	}

	/**
	 * @return
	 */
//	public ResourceServerTokenServices tokenStoreResourceServerTokenServices() {
//		TokenStoreResourceServerTokenServices tokenService = new TokenStoreResourceServerTokenServices();
//		tokenService.setTokenStore(tokenStore());
//		return tokenService;
//	}

//	@Bean
//	public TokenStore tokenStore() {
//		return new RedisTokenStore(connectionFactory);
//	}

	static class BearerTokenRequestMatcher implements RequestMatcher {
		private boolean matchHeader(HttpServletRequest request) {
			String authHeader = request.getHeader("Authorization");
			return StringUtils.startsWithIgnoreCase(authHeader, OAuth2AccessToken.BEARER_TYPE);
		}

		@Override
		public boolean matches(HttpServletRequest request) {
			return matchHeader(request) || matchParameter(request);
		}

		private boolean matchParameter(HttpServletRequest request) {
			return StringUtils.isNotBlank(request.getParameter(OAuth2AccessToken.ACCESS_TOKEN));
		}
	}

}
