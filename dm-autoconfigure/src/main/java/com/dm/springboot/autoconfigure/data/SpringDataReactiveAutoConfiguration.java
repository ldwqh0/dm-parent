package com.dm.springboot.autoconfigure.data;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver;
import org.springframework.data.web.ReactiveSortHandlerMethodArgumentResolver;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;

@ConditionalOnClass(ServerWebExchange.class)
public class SpringDataReactiveAutoConfiguration {

    @Bean
    @ConditionalOnClass(Pageable.class)
    public HandlerMethodArgumentResolver pr() {
        return new ReactivePageableHandlerMethodArgumentResolver();
    }

    @Bean
    public HandlerMethodArgumentResolver sr() {
        return new ReactiveSortHandlerMethodArgumentResolver();
    }
}
