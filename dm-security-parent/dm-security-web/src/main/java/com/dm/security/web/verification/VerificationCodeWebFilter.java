package com.dm.security.web.verification;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.dm.security.verification.VerificationCode;
import com.dm.security.verification.VerificationCodeStorage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 验证码验证过滤器
 * 
 * @author LiDong
 *
 */
public class VerificationCodeWebFilter implements WebFilter, InitializingBean {

    /**
     * 指定哪些请求需要进行验证码过滤
     */
    private List<ServerWebExchangeMatcher> requestMathcers = new LinkedList<>();

    private VerificationCodeStorage storage = null;

    private ObjectMapper om = new ObjectMapper();

    private String verifyIdParameterName = "verifyId";

    private String verifyCodeParameterName = "verifyCode";

    @Autowired(required = false)
    public void setObjectMapper(ObjectMapper om) {
        this.om = om;
    }

    @Autowired
    public void setVerificationCodeStorage(VerificationCodeStorage storage) {
        this.storage = storage;
    }

    public void requestMatcher(ServerWebExchangeMatcher requestMatcher) {
        this.requestMathcers.add(requestMatcher);
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
        return (!Objects.isNull(savedCode))
                && StringUtils.isNotBlank(savedCode.getCode())
                && StringUtils.equals(savedCode.getCode(), verifyCode);
    }

    private Mono<Boolean> requiresValidation(ServerWebExchange exchange) {
        return Flux.fromIterable(requestMathcers)
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
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("path", uri);
            result.put("error", HttpStatus.FORBIDDEN.getReasonPhrase());
            result.put("message", "验证码输入错误");
            result.put("status", HttpStatus.FORBIDDEN.value());
            result.put("timestamp", ZonedDateTime.now());
            byte[] bf = null;
            try {
                bf = om.writeValueAsBytes(result);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return Mono.just(response.bufferFactory().wrap(bf));
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(storage, "the storage can not be null");
        if (Objects.isNull(om)) {
            this.om = new ObjectMapper();
        }
    }
}
