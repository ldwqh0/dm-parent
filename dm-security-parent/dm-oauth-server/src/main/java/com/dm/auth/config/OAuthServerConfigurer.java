package com.dm.auth.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.common.util.ProxyCreator;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.approval.ApprovalStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import com.dm.security.oauth2.provider.code.DmAuthorizationCodeTokenGranter;
import com.dm.security.oauth2.provider.endpoint.DmOauthRedirectResolver;
import com.dm.security.oauth2.support.service.ClientService;
import com.dm.security.oauth2.support.service.UserClientApprovalService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableAuthorizationServer
@Configuration
public class OAuthServerConfigurer extends AuthorizationServerConfigurerAdapter {

    @Autowired
//    @Qualifier("clientInfoService")
    private ClientService clientDetailsService;

    @Autowired
    private UserClientApprovalService userApprovalService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private AuthorizationServerEndpointsConfigurer endpoints;

    /**
     * 这个主要是针对授权服务的配置，也就是针对/oauth/token这个地址的相关配置，比如添加过滤器什么的
     */
    @SuppressWarnings("deprecation")
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 通常情况下,Spring Security获取token的认证模式是基于http basic的,
        // 也就是client的client_id和client_secret是通过http的header或者url模式传递的，
        // 也就是通过http请求头的 Authorization传递，具体的请参考http basic
        // 或者http://client_id:client_secret@server/oauth/token的模式传递的
        // 当启用这个配置之后，server可以从表单参数中获取相应的client_id和client_secret信息
        security.allowFormAuthenticationForClients();
        security.passwordEncoder(NoOpPasswordEncoder.getInstance());
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
        // 重新配置tokenGranter
        endpoints.tokenGranter(tokenGranter(endpoints.getTokenServices(), endpoints.getAuthorizationCodeServices()));
        endpoints.userApprovalHandler(userApprovalHandler()); // 用户授权处理逻辑
        // 如果使用自定义的tokenService,以下的配置都不可用，需要在tokenService中重新配置
        endpoints.tokenStore(tokenStore());
        // tokenConverter只在使用Jwt的时候有用。
        // endpoints.accessTokenConverter(accessTokenConverter());
        // 指定是否可以重用refreshToken
        endpoints.reuseRefreshTokens(false);
        // 如果要使用RefreshToken可用，必须指定UserDetailsService
        endpoints.userDetailsService(userDetailsService);
        // 指定redirectResolver
        endpoints.redirectResolver(new DmOauthRedirectResolver());
        // 使用这个authenticationManager可以启用密码模式
        endpoints.authenticationManager(authenticationManager);
        this.endpoints = endpoints;
    }

//    @Bean
//    public AccessTokenConverter accessTokenConverter() {
//        DefaultAccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();
//        tokenConverter.setUserTokenConverter(userTokenConverter());
//        return tokenConverter;
//    }

//    @Bean
//    public UserAuthenticationConverter userTokenConverter() {
//        UserDetailsAuthenticationConverter userTokenConverter = new UserDetailsAuthenticationConverter();
//        return userTokenConverter;
//    }

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

    @Bean
    public ResourceServerTokenServices resourceServerTokenServices() {
        return ProxyCreator.getProxy(ResourceServerTokenServices.class,
                () -> endpoints.getResourceServerTokenServices());
    }

    private TokenGranter tokenGranter(AuthorizationServerTokenServices tokenService,
            AuthorizationCodeServices authorizationCodeServices) {
        return new CompositeTokenGranter(getDefaultTokenGranters(tokenService, authorizationCodeServices));
    }

    private List<TokenGranter> getDefaultTokenGranters(AuthorizationServerTokenServices tokenServices,
            AuthorizationCodeServices authorizationCodeServices) {
//        ClientDetailsService clientDetails = clientDetailsService();
//        AuthorizationServerTokenServices tokenServices = tokenServices ;
//        AuthorizationCodeServices authorizationCodeServices = authorizationCodeServices ;
        OAuth2RequestFactory requestFactory = requestFactory();

        List<TokenGranter> tokenGranters = new ArrayList<TokenGranter>();

        DmAuthorizationCodeTokenGranter dmAuthorizationCodeTokenGranter = new DmAuthorizationCodeTokenGranter(
                tokenServices, authorizationCodeServices, clientDetailsService, requestFactory);
        // 对于某些clientId，不进行URL检查
        dmAuthorizationCodeTokenGranter.addWhiteList("zuul");
        // 添加自定义的tokenGranter白名单
        tokenGranters.add(dmAuthorizationCodeTokenGranter);
        tokenGranters.add(new RefreshTokenGranter(tokenServices, clientDetailsService, requestFactory));
        ImplicitTokenGranter implicit = new ImplicitTokenGranter(tokenServices, clientDetailsService, requestFactory);
        tokenGranters.add(implicit);
        tokenGranters.add(new ClientCredentialsTokenGranter(tokenServices, clientDetailsService, requestFactory));
        if (authenticationManager != null) {
            tokenGranters.add(new ResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices,
                    clientDetailsService, requestFactory));
        }
        return tokenGranters;
    }

}
