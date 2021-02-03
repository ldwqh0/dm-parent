package com.dm.security.web.server.util.matcher;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

public class RegexServerWebExchangeMatcher implements ServerWebExchangeMatcher {

    private final Pattern pattern;
    private final HttpMethod httpMethod;

    public RegexServerWebExchangeMatcher(String pattern, String httpMethod) {
        this(pattern, httpMethod, false);
    }

    public RegexServerWebExchangeMatcher(String pattern, String httpMethod, boolean caseInsensitive) {
        if (caseInsensitive) {
            this.pattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        } else {
            this.pattern = Pattern.compile(pattern);
        }
        this.httpMethod = StringUtils.isNotBlank(httpMethod) ? HttpMethod.valueOf(httpMethod) : null;
    }

    @Override
    public Mono<MatchResult> matches(ServerWebExchange exchange) {
        return Mono.just(exchange)
                .map(ServerWebExchange::getRequest)
                .filter(request -> httpMethod != null && request.getMethod() != null
                        && httpMethod.equals(request.getMethod()))
                .map(request -> request.getURI().getPath())
                .filter(url -> pattern.matcher(url).matches())
                .flatMap(url -> MatchResult.match())
                .or(MatchResult.notMatch());
    }

}
