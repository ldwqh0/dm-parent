package com.dm.security.authorization;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
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

import com.dm.security.authentication.ResourceAuthorityAttribute;
import com.dm.security.authentication.ResourceAuthorityService;
import com.dm.security.authentication.UriResource;
import com.dm.security.authentication.UriResource.MatchType;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ServerHttpRequestReactiveAuthorizationManager
        implements ReactiveAuthorizationManager<AuthorizationContext> {

    private ResourceAuthorityService resourceAuthorityService;

    @Autowired(required = false)
    public void setResourceAuthorityService(ResourceAuthorityService resourceAuthorityService) {
        this.resourceAuthorityService = resourceAuthorityService;
    }

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext context) {
        ServerWebExchange exchange = context.getExchange();
        return Mono.defer(() -> {
            Collection<ResourceAuthorityAttribute> attributes = resourceAuthorityService.listAll();
            Mono<List<ResourceAuthorityAttribute>> matches = Flux
                    .concat(Flux.fromIterable(attributes).map(attribute -> matches(exchange, attribute)))
                    .filter(mc -> mc.getMatchResult().isMatch())
                    .map(MatchContext::getResourceAuthorityAttribute)
                    .collectList();
            return Mono.zip(matches, authentication).map(a -> {
                List<ResourceAuthorityAttribute> list = a.getT1();
                Authentication au = a.getT2();
                final Set<String> currentAuthorities = au.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .filter(StringUtils::isNotEmpty).collect(Collectors.toSet());
                return check(list, currentAuthorities);
            });
        });

    }

    /**
     * 判断是否有访问权限
     * 
     * @param list
     * @param currentAuthorities
     * @return
     */
    private AuthorizationDecision check(List<ResourceAuthorityAttribute> list, Set<String> currentAuthorities) {
        int grantCount = 0;
        for (ResourceAuthorityAttribute attribute : list) {
            // 如果资源允许任何被授权的用户访问,并且用户不是匿名用户，投票+1
//          if (attribute.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
//              grantCount++;
//          }
            // 如果拒绝列表中包含某一个当前用户的所属角色，立即返回
            if (CollectionUtils.isNotEmpty(currentAuthorities)
                    && CollectionUtils.isNotEmpty(attribute.getDenyAuthorities())
                    && CollectionUtils.containsAny(currentAuthorities,
                            attribute.getDenyAuthorities())) {
                return new AuthorizationDecision(false);
            }

            // 匿名用户的可访问权限包含在显示的权限配置中
            if (CollectionUtils.isNotEmpty(currentAuthorities)
                    && CollectionUtils.isNotEmpty(attribute.getAccessAuthority())
                    && CollectionUtils.containsAny(currentAuthorities,
                            attribute.getAccessAuthority())) {
                grantCount++;
//                if (validScope(attribute, authentication)) {
//                    grantCount++;
//                }
            }
        }
        return new AuthorizationDecision(grantCount > 0);
    }

    // 判断是否匹配
    private Mono<MatchContext> matches(ServerWebExchange exchange, ResourceAuthorityAttribute attribute) {
        UriResource resource = attribute.getResource();
        MatchType matchType = resource.getMatchType();
        if (MatchType.ANT_PATH.equals(matchType)) {
            return ServerWebExchangeMatchers.pathMatchers(HttpMethod.resolve(resource.getMethod()), resource.getPath())
                    .matches(exchange)
                    .map(i -> new MatchContext(i, attribute));
        }
        if (MatchType.REGEXP.equals(matchType)) {
//          return new RegexRequestMatcher(resource.getPath(), resource.getMethod(), true).matches(ex);
        }
        return Mono.empty();
    }

    class MatchContext {
        MatchResult matchResult;
        ResourceAuthorityAttribute resourceAuthorityAttribute;

        public MatchResult getMatchResult() {
            return matchResult;
        }

        public ResourceAuthorityAttribute getResourceAuthorityAttribute() {
            return resourceAuthorityAttribute;
        }

        public MatchContext(MatchResult matchResult, ResourceAuthorityAttribute raa) {
            super();
            this.matchResult = matchResult;
            this.resourceAuthorityAttribute = raa;
        }

    }

}
