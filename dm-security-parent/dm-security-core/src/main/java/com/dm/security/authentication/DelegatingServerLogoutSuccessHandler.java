package com.dm.security.authentication;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 一个基于代理的登出处理器
 * 
 * @author ldwqh0@outlook.com
 *
 */
public class DelegatingServerLogoutSuccessHandler implements ServerLogoutSuccessHandler {
    private final List<DelegateLogoutSuccessEntry> delegateLogoutSuccessHandler;
    private ServerLogoutSuccessHandler defaultLogoutSuccessHandler;

    public DelegatingServerLogoutSuccessHandler(DelegateLogoutSuccessEntry... entry) {
        this.delegateLogoutSuccessHandler = Arrays.asList(entry);
    }

    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
        return Flux.fromIterable(this.delegateLogoutSuccessHandler)
                .filterWhen(entry -> isMatch(exchange.getExchange(), entry))
                .next()
                .map(entry -> entry.getLogoutSuccessHandler())
                .defaultIfEmpty(this.defaultLogoutSuccessHandler)
                .flatMap(entryPoint -> entryPoint.onLogoutSuccess(exchange, authentication));
    }

    private Mono<Boolean> isMatch(ServerWebExchange exchange, DelegateLogoutSuccessEntry entry) {
        ServerWebExchangeMatcher matcher = entry.getMatcher();
        return matcher.matches(exchange)
                .map(result -> result.isMatch());
    }
}
