/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dm.security.web.authorization;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.HttpBasicServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.HttpStatusServerAccessDeniedHandler;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.security.Principal;

/**
 * <p>一个特殊的异常处理器，会在适当的时候返回登录</p>
 * Spring默认的{@link org.springframework.security.web.server.authorization.ExceptionTranslationWebFilter}
 * 的行为和{@link org.springframework.security.web.access.ExceptionTranslationFilter}行为不一致，
 * 具体表现在当我们同时配置匿名用户访问时，在不登录时访问某个受保护的资时，源基于Web的ExceptionTranslationFilter会转到authenticationEntryPoint以便请求登录
 * 而基于WebFlux的ExceptionTranslationWebFilter会返回403错误。
 * <p>这个filter的目的是修复这种不一致</p>
 * <p>这个filter要添加到SecurityWebFiltersOrder.EXCEPTION_TRANSLATION之后</p>
 *
 * <code>
 * DmExceptionTranslationWebFilter exceptionTranslationWebFilter = new DmExceptionTranslationWebFilter();<br>
 * http.addFilterAfter(exceptionTranslationWebFilter, SecurityWebFiltersOrder.EXCEPTION_TRANSLATION);<br>
 * </code>
 *
 * @author Li Dong
 * @since 5.0
 */
public class DmExceptionTranslationWebFilter implements WebFilter {
    private ServerAuthenticationEntryPoint authenticationEntryPoint = new HttpBasicServerAuthenticationEntryPoint();

    private ServerAccessDeniedHandler accessDeniedHandler = new HttpStatusServerAccessDeniedHandler(HttpStatus.FORBIDDEN);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange)
            .onErrorResume(AccessDeniedException.class, denied -> exchange.getPrincipal()
                // 仅仅获取不是匿名用户的权限对象
                .filter(this::isNotAnonymous)
                // 如果principal是一个匿名用户,则会被上一步给过滤掉，此时的principal为空，会跳转到授权
                .switchIfEmpty(commenceAuthentication(exchange, denied))
                // 对非匿名用户进行校验部通过的处理
                .flatMap(principal -> this.accessDeniedHandler.handle(exchange, denied))
            );
    }

    private boolean isNotAnonymous(Principal principal) {
        return !AnonymousAuthenticationToken.class.isAssignableFrom(principal.getClass());
    }

    /**
     * Sets the access denied handler.
     *
     * @param accessDeniedHandler the access denied handler to use. Default is
     *                            HttpStatusAccessDeniedHandler with HttpStatus.FORBIDDEN
     */
    public void setAccessDeniedHandler(ServerAccessDeniedHandler accessDeniedHandler) {
        Assert.notNull(accessDeniedHandler, "accessDeniedHandler cannot be null");
        this.accessDeniedHandler = accessDeniedHandler;
    }

    /**
     * Sets the authentication entry point used when authentication is required
     *
     * @param authenticationEntryPoint the authentication entry point to use. Default is
     *                                 {@link HttpBasicServerAuthenticationEntryPoint}
     */
    public void setAuthenticationEntryPoint(
        ServerAuthenticationEntryPoint authenticationEntryPoint) {
        Assert.notNull(authenticationEntryPoint, "authenticationEntryPoint cannot be null");
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    private <T> Mono<T> commenceAuthentication(ServerWebExchange exchange, AccessDeniedException denied) {
        return this.authenticationEntryPoint.commence(exchange, new AuthenticationCredentialsNotFoundException("Not Authenticated", denied))
            .then(Mono.empty());
    }
}

