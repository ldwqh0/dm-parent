package com.dm.server.gateway.config;


import com.dm.security.core.userdetails.UserDetailsDto;
import com.dm.security.oauth2.authorization.ServerHttpOauth2RequestReactiveAuthorizationManager;
import com.dm.security.oauth2.client.web.server.DmServerOAuth2AuthorizationRequestResolver;
import com.dm.security.web.authorization.DmExceptionTranslationWebFilter;
import com.dm.security.web.server.authentication.ServerLoginSuccessHandler;
import com.dm.security.web.server.util.matcher.NotBearerTokenServerWebExchangeMatcher;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.server.DelegatingServerAuthenticationEntryPoint;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.security.web.server.util.matcher.MediaTypeServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import javax.net.ssl.SSLException;
import java.util.Collections;

@Configuration
public class SecurityConfiguration {

    private ReactiveClientRegistrationRepository clientRegistrationRepository;

    private ServerOAuth2AuthorizedClientRepository authorizedClientRepository;

    @Autowired
    public void setReactiveClientRegistrationRepository(ReactiveClientRegistrationRepository reactiveClientRegistrationRepository) {
        this.clientRegistrationRepository = reactiveClientRegistrationRepository;
    }

    @Autowired
    public void setAuthorizedClientRepository(ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {
        this.authorizedClientRepository = authorizedClientRepository;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        MediaTypeServerWebExchangeMatcher mediaMatcher = new MediaTypeServerWebExchangeMatcher(MediaType.APPLICATION_JSON);
        mediaMatcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
        // 指定权限检察器
        http.authorizeExchange().anyExchange().access(reactiveAuthorizationManager());
        // 设置匿名用户
        UserDetailsDto anonymousUser = new UserDetailsDto();
        anonymousUser.setId(2L);
        anonymousUser.setUsername("ANONYMOUS");
        http.anonymous().principal(anonymousUser).authorities("内置分组_ROLE_ANONYMOUS");
        // 配置oauth2登录
        http.oauth2Login(customizer -> {
            DmServerOAuth2AuthorizationRequestResolver requestResolver = new DmServerOAuth2AuthorizationRequestResolver(clientRegistrationRepository);
            requestResolver.setDefaultPrefix("/");
            customizer.authorizationRequestResolver(requestResolver);
            customizer.authenticationSuccessHandler(loginSuccessHandler());
        });
        // 配置csrf
        http.csrf().csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse());

//        http.oauth2ResourceServer().opaqueToken().introspectionUri()

        // 重新配置一个异常处理器，这个异常处理器在用户没有登录是，转入登录入口点
        DmExceptionTranslationWebFilter exceptionTranslationWebFilter = new DmExceptionTranslationWebFilter();
        // 指定一个返回401的入口
        DelegatingServerAuthenticationEntryPoint.DelegateEntry entry = new DelegatingServerAuthenticationEntryPoint.DelegateEntry(mediaMatcher, new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED));
        DelegatingServerAuthenticationEntryPoint delegationEntrypoint = new DelegatingServerAuthenticationEntryPoint(entry);
        // 指定默认跳转oauth2等的入口
        delegationEntrypoint.setDefaultEntryPoint(new RedirectServerAuthenticationEntryPoint("/oauth2/authorization/oauth2"));
        exceptionTranslationWebFilter.setAuthenticationEntryPoint(delegationEntrypoint);
        http.addFilterAfter(exceptionTranslationWebFilter, SecurityWebFiltersOrder.EXCEPTION_TRANSLATION);
        return http.build();
    }

    /**
     * 当我们使用csrf时，如果使用cookie作为容器，由于在请求链接中，没有地方订阅csrf token,所以不会将csrf写到token中
     *
     * @return
     */
    @Bean
    public WebFilter addCsrfTokenFilter() {
        final NotBearerTokenServerWebExchangeMatcher addCsrfTokenCookieRequestMatcher = new NotBearerTokenServerWebExchangeMatcher();
        return (exchange, next) -> Mono.just(exchange)
            .filterWhen(ex -> addCsrfTokenCookieRequestMatcher.matches(ex).map(MatchResult::isMatch))
            .flatMap(ex -> ex.<Mono<CsrfToken>>getAttribute(CsrfToken.class.getName()))
            .doOnNext(ex -> {
                // nothing ,only subscribe for generate csrf cookie.
            })
            .then(next.filter(exchange));
    }

    @Bean
    public ReactiveAuthorizationManager<AuthorizationContext> reactiveAuthorizationManager() {
        ServerHttpOauth2RequestReactiveAuthorizationManager reactiveAuthorizationManager = new ServerHttpOauth2RequestReactiveAuthorizationManager();
        reactiveAuthorizationManager.setAuthorizedClientRepository(authorizedClientRepository);
        return reactiveAuthorizationManager;
    }

    @Bean
    public ServerLoginSuccessHandler loginSuccessHandler() {
        return new ServerLoginSuccessHandler();
    }

}

