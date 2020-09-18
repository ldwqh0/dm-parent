package com.dm.security.web.server.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.util.matcher.MediaTypeServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

public class ServerLoginSuccessHandler extends RedirectServerAuthenticationSuccessHandler {


    private Jackson2JsonEncoder jsonEncoder;

    private final ServerWebExchangeMatcher mediaMatcher;

    public ServerLoginSuccessHandler() {
        MediaTypeServerWebExchangeMatcher matcher = new MediaTypeServerWebExchangeMatcher(MediaType.APPLICATION_JSON);
        matcher.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
        this.mediaMatcher = matcher;
    }

    @Autowired
    public void setJsonEncoder(ObjectMapper objectMapper) {
        this.jsonEncoder = new Jackson2JsonEncoder(objectMapper);
    }

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange filterExchange, Authentication authentication) {
        ServerWebExchange exchange = filterExchange.getExchange();
        return mediaMatcher.matches(exchange).flatMap(matchResult -> {
            if (matchResult.isMatch()) {
                ServerHttpResponse response = filterExchange.getExchange().getResponse();
                response.setStatusCode(HttpStatus.OK);
                response.getHeaders().put("Content-Type", Collections.singletonList("application/json;charset=utf-8"));
                return response.writeWith(build(response.bufferFactory(), authentication.getPrincipal()));
            } else {
                return super.onAuthenticationSuccess(filterExchange, authentication);
            }
        });
    }

    private Flux<DataBuffer> build(DataBufferFactory bufferFactory, Object obj) {
        return jsonEncoder.encode(Mono.just(obj), bufferFactory, ResolvableType.forInstance(obj), MediaType.APPLICATION_JSON, null);
    }
}
