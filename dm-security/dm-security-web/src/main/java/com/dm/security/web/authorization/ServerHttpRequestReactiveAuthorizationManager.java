package com.dm.security.web.authorization;

import com.dm.collections.CollectionUtils;
import com.dm.security.authentication.ResourceAuthorityAttribute;
import com.dm.security.authentication.ResourceAuthorityService;
import com.dm.security.authentication.UriResource;
import com.dm.security.authentication.UriResource.MatchType;
import com.dm.security.web.server.util.matcher.RegexServerWebExchangeMatcher;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ServerHttpRequestReactiveAuthorizationManager
    implements ReactiveAuthorizationManager<AuthorizationContext> {

    private ResourceAuthorityService resourceAuthorityService = null;

    @Autowired(required = false)
    public void setResourceAuthorityService(ResourceAuthorityService resourceAuthorityService) {
        this.resourceAuthorityService = resourceAuthorityService;
    }

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext context) {
        return Mono.defer(() -> {
            final ServerWebExchange exchange = context.getExchange();
            final Collection<ResourceAuthorityAttribute> attributes = resourceAuthorityService.listAll();
            // 获取所有匹配到的数据
            Mono<List<ResourceAuthorityAttribute>> matches = Flux.fromIterable(attributes)
                .filterWhen(attribute -> matches(exchange, attribute).map(MatchResult::isMatch))
                .collectList();
            return Mono.zip(matches, authentication)
                .flatMap(result -> check(result.getT1(), result.getT2(), context));
        });
    }

    /**
     * 判断是否有访问权限
     *
     * @param list           对象列表
     * @param authentication 授权对象
     * @param context        授权上下文
     * @return 授权结果
     */
    private Mono<AuthorizationDecision> check(List<ResourceAuthorityAttribute> list, Authentication authentication,
                                              AuthorizationContext context) {
        Flux<Boolean> checkResult = Flux.empty();
//        Flux.fromIterable(authentication.getAuthorities());
        final Set<String> currentAuthorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .filter(StringUtils::isNotEmpty)
            .collect(Collectors.toSet());
        for (ResourceAuthorityAttribute attribute : list) {
            // 如果拒绝列表中包含某一个当前用户的所属角色，立即返回
            if (CollectionUtils.isNotEmpty(currentAuthorities)
                && CollectionUtils.isNotEmpty(attribute.getDenyAuthorities())
                && CollectionUtils.containsAny(currentAuthorities, attribute.getDenyAuthorities())) {
                return Mono.just(new AuthorizationDecision(false));
            }
            // 匿名用户的可访问权限包含在显示的权限配置中
            if (CollectionUtils.isNotEmpty(currentAuthorities)
                && CollectionUtils.isNotEmpty(attribute.getAccessAuthority())
                && CollectionUtils.containsAny(currentAuthorities, attribute.getAccessAuthority())) {
                checkResult = checkResult.concatWith(additionalValidate(attribute, authentication, context));
            }
        }
        return checkResult.defaultIfEmpty(Boolean.FALSE)
            .all(Boolean::booleanValue)
            .map(AuthorizationDecision::new);
    }

    /**
     * 进行额外的校验
     *
     * @param attribute
     * @param authentication
     * @return
     */
    protected Mono<Boolean> additionalValidate(ResourceAuthorityAttribute attribute, Authentication authentication,
                                               AuthorizationContext context) {
        return Mono.just(Boolean.TRUE);
    }

    // 判断是否匹配
    private Mono<MatchResult> matches(ServerWebExchange exchange, ResourceAuthorityAttribute attribute) {
        UriResource resource = attribute.getResource();
        MatchType matchType = resource.getMatchType();
        if (MatchType.ANT_PATH.equals(matchType)) {
            return ServerWebExchangeMatchers
                .pathMatchers(HttpMethod.resolve(resource.getMethod()), resource.getPath())
                .matches(exchange);
        }
        if (MatchType.REGEXP.equals(matchType)) {
            return ServerWebExchangeMatchers
                .matchers(new RegexServerWebExchangeMatcher(resource.getPath(), resource.getMethod()))
                .matches(exchange);
        }
        return Mono.empty();
    }
}
