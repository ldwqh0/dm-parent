package com.dm.security.web.server.util.matcher;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * 验证一个请求不是 resource server的请求，即请求头中没有Authorization头信息，参数中没有access_token
 * 
 * @author ldwqh0@outlook.com
 * 
 * @since 1.0.0
 *
 */
public class NotBearerTokenServerWebExchangeMatcher implements ServerWebExchangeMatcher {

    private static final Pattern authorizationPattern = Pattern.compile(
            "^Bearer (?<token>[a-zA-Z0-9-._~+/]+)=*$",
            Pattern.CASE_INSENSITIVE);

    @Override
    public Mono<MatchResult> matches(ServerWebExchange exchange) {
        return Mono.just(exchange)
                .map(ServerWebExchange::getRequest)
                .filter(this::matchBearer)
                .flatMap(i -> MatchResult.notMatch())
                .switchIfEmpty(MatchResult.match());
    }

    /**
     * 匹配bearer请求头和access_token参数
     * 
     * @param request
     * @return
     */
    private boolean matchBearer(ServerHttpRequest request) {
        return matchBearerHeader(request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                || StringUtils.isNotEmpty(request.getQueryParams().getFirst("access_token"));
    }

    /**
     * 匹配bearer请求头
     * 
     * @param header
     * @return
     */
    private boolean matchBearerHeader(String header) {
        return StringUtils.isNotBlank(header) && authorizationPattern.matcher(header).matches();
    }
}
