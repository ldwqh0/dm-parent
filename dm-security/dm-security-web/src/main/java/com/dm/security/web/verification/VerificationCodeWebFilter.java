package com.dm.security.web.verification;

import com.dm.collections.CollectionUtils;
import com.dm.security.verification.VerificationCode;
import com.dm.security.verification.VerificationCodeStorage;
import com.dm.security.web.authentication.AuthenticationObjectMapperFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * 验证码验证过滤器
 *
 * @author LiDong
 */
public class VerificationCodeWebFilter implements WebFilter {

    /**
     * 指定哪些请求需要进行验证码过滤
     */
    private final List<ServerWebExchangeMatcher> requestMatchers = new LinkedList<>();

    private VerificationCodeStorage storage = null;

    private ObjectMapper objectMapper = AuthenticationObjectMapperFactory.getObjectMapper();

    private static final String verifyIdParameterName = "verifyId";

    private static final String verifyCodeParameterName = "verifyCode";

    @Autowired(required = false)
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setVerificationCodeStorage(VerificationCodeStorage storage) {
        this.storage = storage;
    }

    public void requestMatcher(ServerWebExchangeMatcher requestMatcher) {
        this.requestMatchers.add(requestMatcher);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return requiresValidation(exchange)
            .filter(Boolean.TRUE::equals)
            .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
            .flatMap(i -> {
                String verifyId = parseParameter(exchange, verifyIdParameterName);
                String verifyCode = parseParameter(exchange, verifyCodeParameterName);
                if (validate(verifyId, verifyCode)) {
                    storage.remove(verifyId);
                    return chain.filter(exchange);
                } else {
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.FORBIDDEN);
                    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    return response.writeWith(createBuffer(exchange));
                }
            });
    }

    private boolean validate(String verifyId, String verifyCode) {
        VerificationCode savedCode = storage.get(verifyId);
        return (Objects.nonNull(savedCode))
            && StringUtils.isNotBlank(savedCode.getCode())
            && StringUtils.equals(savedCode.getCode(), verifyCode);
    }

    private Mono<Boolean> requiresValidation(ServerWebExchange exchange) {
        return Flux.fromIterable(requestMatchers)
            .flatMap(i -> i.matches(exchange))
            .any(MatchResult::isMatch);
    }

    /**
     * 从请求中解析参数
     *
     * @param exchange
     * @param parameter
     * @return
     */
    private String parseParameter(ServerWebExchange exchange, String parameter) {
        MultiValueMap<String, String> a = exchange.getRequest().getQueryParams();
        List<String> values = a.get(parameter);
        if (CollectionUtils.isNotEmpty(values)) {
            return values.get(0);
        } else {
            return null;
        }
    }

    private Mono<DataBuffer> createBuffer(ServerWebExchange exchange) {
        return Mono.defer(() -> {
            URI uri = exchange.getRequest().getURI();
            ServerHttpResponse response = exchange.getResponse();
            Map<String, Object> result = new HashMap<>();
            result.put("path", uri);
            result.put("error", HttpStatus.FORBIDDEN.getReasonPhrase());
            result.put("message", "验证码输入错误");
            result.put("status", HttpStatus.FORBIDDEN.value());
            result.put("timestamp", ZonedDateTime.now());
            byte[] bf;
            try {
                // TODO 这里不对
                bf = objectMapper.writeValueAsBytes(result);
                return Mono.just(response.bufferFactory().wrap(bf));
            } catch (JsonProcessingException e) {
                return Mono.error(e);
            }

        });
    }

}
