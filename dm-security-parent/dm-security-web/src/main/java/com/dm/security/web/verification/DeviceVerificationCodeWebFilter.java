package com.dm.security.web.verification;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

import com.dm.collections.CollectionUtils;
import com.dm.security.verification.DeviceVerificationCodeStorage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 设备验证过滤器<br>
 * 用于验证向设备发送的验证码
 * 
 * @author LiDong
 *
 */
public class DeviceVerificationCodeWebFilter implements WebFilter, InitializingBean {

    private final List<ServerWebExchangeMatcher> requestMathcers = new ArrayList<>();

    private String verifyIdParameterName = "verifyId";

    private String verifyCodeParameterName = "verifyCode";

    private String verifyCodeKeyParameterName = "email";

    private DeviceVerificationCodeStorage storage;

    private ObjectMapper om = new ObjectMapper();

    @Autowired(required = false)
    public void setObjectMapper(ObjectMapper om) {
        this.om = om;
    }

    public void requestMathcer(ServerWebExchangeMatcher matcher) {
        this.requestMathcers.add(matcher);
    }

    @Autowired
    public void setDeviceVerificationCodeStorage(DeviceVerificationCodeStorage storage) {
        this.storage = storage;
    }

    public void setVerifyIdParameterName(String verifyIdParameterName) {
        this.verifyIdParameterName = verifyIdParameterName;
    }

    public void setVerifyCodeParameterName(String verifyCodeParameterName) {
        this.verifyCodeParameterName = verifyCodeParameterName;
    }

    public void setVerifyCodeKeyParameterName(String verifyCodeKeyParameterName) {
        this.verifyCodeKeyParameterName = verifyCodeKeyParameterName;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return requiresValidation(exchange)
                .filter(Boolean.TRUE::equals)
                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
                .flatMap(i -> render(exchange, chain));
    }

    private Mono<Void> render(ServerWebExchange exchange, WebFilterChain chain) {
        String verifyId = parseParameter(exchange, verifyIdParameterName);
        String verifyKey = parseParameter(exchange, verifyCodeKeyParameterName);
        String verifyCode = parseParameter(exchange, verifyCodeParameterName);
        if (validate(verifyId, verifyKey, verifyCode)) {
            storage.remove(verifyId);
            return chain.filter(exchange);
        } else {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.FORBIDDEN);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return response.writeWith(createBuffer(exchange));
        }
    }

    private Mono<DataBuffer> createBuffer(ServerWebExchange exchange) {
        URI uri = exchange.getRequest().getURI();
        Map<String, Object> result = new LinkedHashMap<>();
        ServerHttpResponse response = exchange.getResponse();
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
    }

    /**
     * 验证参数
     * 
     * @param id
     * @param key
     * @param code
     * @return
     */
    private boolean validate(String id, String key, String code) {
        if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(key) && StringUtils.isNotBlank(code)) {
            return storage.findById(id)
                    .map(i -> {
                        ZonedDateTime expireAt = i.getExpireAt();
                        String savedKey = i.getKey();
                        String savedCode = i.getCode();
                        return ZonedDateTime.now().isBefore(expireAt)
                                && StringUtils.equals(key, savedKey)
                                && StringUtils.equals(code, savedCode);
                    }).orElse(false);
        } else {
            return false;
        }

    }

    /**
     * 判断该请求是否需要验证
     * 
     * @param exchange
     * @return
     */
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

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(storage, "this storage can not be null");
        if (Objects.isNull(om)) {
            om = new ObjectMapper();
        }
    }
}
