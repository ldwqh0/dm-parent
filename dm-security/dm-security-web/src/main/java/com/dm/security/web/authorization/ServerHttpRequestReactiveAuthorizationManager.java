package com.dm.security.web.authorization;

import com.dm.collections.CollectionUtils;
import com.dm.security.authentication.ResourceAuthorityAttribute;
import com.dm.security.authentication.ResourceAuthorityService;
import com.dm.security.authentication.UriResource;
import com.dm.security.authentication.UriResource.MatchType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
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
     * 判断是否有访问权限<br>
     * 权限判断逻辑<br>
     * 一、遍历所有的权限配置<br>
     * 二、依次遍历资源的匹配情况<br>
     * 三、如果资源匹配某个规则，运行该规则。<br>
     * 四、如果检测到某个规则禁止通过，立即禁止通过<br>
     * 五、如果检测到某个规则允许当前请求通过，继续进行下一轮判断<br>
     * 六、如果最终判断结果为允许通过的数量大于1，则通过。
     *
     * @param list           对象列表
     * @param authentication 授权对象
     * @param context        授权上下文
     * @return 授权结果
     */
    private Mono<AuthorizationDecision> check(List<ResourceAuthorityAttribute> list, Authentication authentication,
                                              AuthorizationContext context) {
        return Mono.defer(() -> {
            Flux<Boolean> checkResult = Flux.empty();
            final Set<String> currentAuthorities = authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toSet());
            for (ResourceAuthorityAttribute attribute : list) {
                // 如果针对某个资源的的配置是拒绝访问，立即拒绝
                if (attribute.isDenyAll()) {
                    return Mono.just(new AuthorizationDecision(false));
                }
                // 如果针对某个资源的访问是登录即可访问，判断用户是否登录，
                // 如果显示的配置了登录即可访问，当用户没有登录时，不予处理，根据后面的规则进行处理。
                if (attribute.isAuthenticated() && !isAnonymous(authentication)) {
                    checkResult = checkResult.concatWith(additionalValidate(attribute, authentication, context));
                }
                // 拒绝的列表
                Set<String> denyAuthorities = attribute.getDenyAuthorities();
                // 允许的列表
                Set<String> accessAuthorities = attribute.getAccessAuthorities();
                // 如果拒绝列表中包含某一个当前用户的所属角色，立即返回
                if (CollectionUtils.isNotEmpty(currentAuthorities)
                    && CollectionUtils.isNotEmpty(denyAuthorities)
                    && CollectionUtils.containsAny(currentAuthorities, denyAuthorities)) {
                    return Mono.just(new AuthorizationDecision(false));
                }
                // 匿名用户的可访问权限包含在显示的权限配置中
                if (CollectionUtils.isNotEmpty(currentAuthorities)
                    && CollectionUtils.isNotEmpty(accessAuthorities)
                    && CollectionUtils.containsAny(currentAuthorities, accessAuthorities)) {
                    checkResult = checkResult.concatWith(additionalValidate(attribute, authentication, context));
                }
            }
            return checkResult.defaultIfEmpty(Boolean.FALSE)
                .all(Boolean::booleanValue)
                .map(AuthorizationDecision::new);
        });
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

    /**
     * 检测是否匿名用户
     */
    private boolean isAnonymous(Object principal) {
        return AnonymousAuthenticationToken.class.isAssignableFrom(principal.getClass());
    }

    // 判断是否匹配
    private Mono<MatchResult> matches(ServerWebExchange exchange, ResourceAuthorityAttribute attribute) {
        UriResource resource = attribute.getResource();
        MatchType matchType = resource.getMatchType();
        if (MatchType.ANT_PATH.equals(matchType)) {
            return resource.getMethod()
                .map(method -> ServerWebExchangeMatchers.pathMatchers(method, resource.getUri()).matches(exchange))
                .orElseGet(() -> ServerWebExchangeMatchers.pathMatchers(resource.getUri()).matches(exchange));
        }
        if (MatchType.REGEXP.equals(matchType)) {
            //TODO 正则表达式还不完美
            // return ServerWebExchangeMatchers
            // .matchers(new RegexServerWebExchangeMatcher(resource.getUri(), resource.getMethod()))
            // .matches(exchange);
        }
        return Mono.empty();
    }
}
