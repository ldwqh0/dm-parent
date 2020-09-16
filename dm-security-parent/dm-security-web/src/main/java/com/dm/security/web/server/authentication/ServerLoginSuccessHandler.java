package com.dm.security.web.server.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.util.matcher.MediaTypeServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;

public class ServerLoginSuccessHandler extends RedirectServerAuthenticationSuccessHandler {

    public ObjectMapper objectMapper;

    public ServerWebExchangeMatcher mediaMatcher;

    public ServerLoginSuccessHandler() {
        MediaTypeServerWebExchangeMatcher matcher = new MediaTypeServerWebExchangeMatcher(MediaType.APPLICATION_JSON);
        matcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
        this.mediaMatcher = matcher;
    }


    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private final DefaultDataBufferFactory defaultDataBufferFactory = new DefaultDataBufferFactory();

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange filterExchange, Authentication authentication) {
        ServerWebExchange exchange = filterExchange.getExchange();
        return mediaMatcher.matches(exchange).flatMap(matchResult -> {
            if (matchResult.isMatch()) {
                ServerHttpResponse response = filterExchange.getExchange().getResponse();
                response.setStatusCode(HttpStatus.OK);
                response.getHeaders().put("Content-Type", Collections.singletonList("application/json;charset=utf-8"));
                return response.writeWith(this.build(authentication.getPrincipal()));
            } else {
                return super.onAuthenticationSuccess(filterExchange, authentication);
            }
        });
    }

    private Mono<DataBuffer> build(Object obj) {
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(obj);
            return Mono.just(defaultDataBufferFactory.wrap(bytes));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
