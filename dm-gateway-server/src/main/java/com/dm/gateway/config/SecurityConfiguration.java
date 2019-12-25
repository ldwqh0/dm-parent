package com.dm.gateway.config;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.DelegatingServerAuthenticationEntryPoint;
import org.springframework.security.web.server.DelegatingServerAuthenticationEntryPoint.DelegateEntry;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.security.web.server.util.matcher.MediaTypeServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult;
import org.springframework.web.server.WebFilter;
import com.dm.security.oauth2.authorization.ServerHttpOauth2RequestReactiveAuthorizationManager;
import com.dm.security.oauth2.server.resource.introspection.ReactiveUserInfoOpaqueTokenIntrospector;
import com.dm.security.web.server.util.matcher.NotBearerTokenServerWebExchangeMatcher;

import reactor.core.publisher.Mono;

/**
 * 网关安全配置
 * 
 * @author ldwqh0@outlook.com
 * 
 * @since 1.0.0
 *
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Value("${spring.security.oauth2.resourceserver.opaquetoken.introspection-uri}")
    private String iu;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        MediaTypeServerWebExchangeMatcher mediaMathcer = new MediaTypeServerWebExchangeMatcher(
                MediaType.APPLICATION_JSON);
        mediaMathcer.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
        DelegatingServerAuthenticationEntryPoint delegationEntripoint = new DelegatingServerAuthenticationEntryPoint(
                new DelegateEntry(mediaMathcer,
                        new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED)));
        delegationEntripoint
                .setDefaultEntryPoint(new RedirectServerAuthenticationEntryPoint("/oauth2/authorization/oauth2"));
        http.authorizeExchange().anyExchange().access(reactiveAuthorizationManager());
        // 重新配置入口点
        http.exceptionHandling().authenticationEntryPoint(delegationEntripoint);
        // 整合oauth2登录
        http.oauth2Login();// .authenticationSuccessHandler( );
        http.csrf().csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse());
        // 网关自身也是资源服务器
        http.oauth2ResourceServer().opaqueToken();
        return http.build();
    }

    /**
     * 当我们使用csrf时，如果使用cookie作为容器，由于在请求链接中，没有地方订阅csrftoken,所以不会将csrf写到token中
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
        return new ServerHttpOauth2RequestReactiveAuthorizationManager();
    }

    @Bean
    public ReactiveUserInfoOpaqueTokenIntrospector reactiveOpaqueTokenIntrospector() {
        return new ReactiveUserInfoOpaqueTokenIntrospector(iu);
    }

}