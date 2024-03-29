package com.dm.security.web.verification;

import com.dm.collections.CollectionUtils;
import com.dm.security.verification.DeviceVerificationCodeStorage;
import com.dm.security.web.authentication.AuthenticationObjectMapperFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备验证过滤器<br>
 * 用于验证向设备发送的验证码
 *
 * @author LiDong
 */
public class DeviceVerificationCodeWebFilter implements WebFilter {

    private final List<ServerWebExchangeMatcher> requestMatchers = new ArrayList<>();

    private String verifyIdParameterName = "verifyId";

    private String verifyCodeParameterName = "verifyCode";

    private String verifyCodeKeyParameterName = "email";

    private final DeviceVerificationCodeStorage storage;

    private final ObjectMapper objectMapper;

    public DeviceVerificationCodeWebFilter(DeviceVerificationCodeStorage storage, ObjectMapper objectMapper) {
        this.storage = storage;
        this.objectMapper = objectMapper;
    }

    public DeviceVerificationCodeWebFilter(DeviceVerificationCodeStorage storage) {
        this(storage, AuthenticationObjectMapperFactory.getObjectMapper());
    }

    public void requestMatcher(ServerWebExchangeMatcher matcher) {
        this.requestMatchers.add(matcher);
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
        try {
            // TODO 这里不对
            return Mono.just(response.bufferFactory().wrap(objectMapper.writeValueAsBytes(result)));
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }

    /**
     * 验证参数
     *
     * @param id   校验验证码
     * @param key  要验证的key
     * @param code 眼验证的code
     * @return 验证成功返回true, 验证失败返回false
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
     * @param exchange 要验证的请求
     * @return 验证结果
     */
    private Mono<Boolean> requiresValidation(ServerWebExchange exchange) {
        return Flux.fromIterable(requestMatchers)
            .flatMap(i -> i.matches(exchange))
            .any(MatchResult::isMatch);
    }

    /**
     * 从请求中解析参数
     *
     * @param exchange  a {@link ServerWebExchange}
     * @param parameter 要解析的参数名称
     * @return 解析出的参数
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
}
