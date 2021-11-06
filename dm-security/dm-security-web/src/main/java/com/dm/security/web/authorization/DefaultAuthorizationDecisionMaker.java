package com.dm.security.web.authorization;

import com.dm.collections.CollectionUtils;
import com.dm.security.authentication.ResourceAuthorityAttribute;
import com.dm.security.authentication.ResourceAuthorityService;
import com.dm.security.authentication.UriResource;
import com.dm.security.authentication.UriResource.MatchType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 授权决策器
 *
 * @author ldwqh0@outlook.com
 */
public class DefaultAuthorizationDecisionMaker implements AuthorizationDecisionMaker {

    private final ResourceAuthorityService authorityService;

    public DefaultAuthorizationDecisionMaker(ResourceAuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    /**
     * 检测某个对象是否具有访问指定资源的权限
     */
    @Override
    public boolean check(Authentication authentication, HttpServletRequest request) {
        // 获取所有的授权对象
        Collection<ResourceAuthorityAttribute> attributes = authorityService.listAll();
        // 获取要检查的授权的角色
        final Set<String> currentAuthorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .filter(StringUtils::isNotEmpty).collect(Collectors.toSet());

        // 所有匹配到的资源
        List<ResourceAuthorityAttribute> allMatches = attributes.stream()
            .filter(attribute -> matches(request, attribute))
            .collect(Collectors.toList());
        // 投票表决结果
        int grantCount = 0;
        for (ResourceAuthorityAttribute attribute : allMatches) {
            // 如果拒绝列表中包含某一个当前用户的所属角色，立即返回
            if (CollectionUtils.isNotEmpty(currentAuthorities)
                && CollectionUtils.isNotEmpty(attribute.getDenyAuthorities())
                && CollectionUtils.containsAny(currentAuthorities, attribute.getDenyAuthorities())) {
                return false;
            }

            // 匿名用户的可访问权限包含在显示的权限配置中
            if (CollectionUtils.isNotEmpty(currentAuthorities)
                && CollectionUtils.isNotEmpty(attribute.getAccessAuthorities())
                && CollectionUtils.containsAny(currentAuthorities, attribute.getAccessAuthorities())) {
                if (validScope(attribute, authentication)) {
                    grantCount++;
                }
            }
        }
        return grantCount > 0;
    }

    // 判断某个请求是否符合匹配指定资源
    private boolean matches(HttpServletRequest request, ResourceAuthorityAttribute attribute) {
        UriResource resource = attribute.getResource();
        MatchType matchType = resource.getMatchType();
        final String uri = resource.getUri();
        if (MatchType.ANT_PATH.equals(matchType)) {
            return resource.getMethod()
                .map(method -> new AntPathRequestMatcher(uri, method.toString()))
                .orElseGet(() -> new AntPathRequestMatcher(uri))
                .matches(request);
        }
        if (MatchType.REGEXP.equals(matchType)) {
            throw new RuntimeException("正则表达式不完美");
            // return new RegexRequestMatcher(resource.getUri(), resource.getMethod().toString(), true).matches(request);
        }
        return false;
    }

    /**
     * 验证当前的授权对象是否允许访问指定资源scope的权限
     *
     * @param attribute      要验证的对象
     * @param authentication 当前对象
     * @return 验证成功返回 true, 验证失败返回 false
     */
    private boolean validScope(ResourceAuthorityAttribute attribute, Authentication authentication) {
        // TODO 这里进行scope校验
        /*
         if (authentication instanceof OAuth2Authentication) {
         Set<String> resourceScope = attribute.getScope();
         Set<String> requestScopes = ((OAuth2Authentication)
         authentication).getOAuth2Request().getScope();
         if (CollectionUtils.isNotEmpty(resourceScope)) {
         return CollectionUtils.containsAny(requestScopes, resourceScope);
         }
         }
         */
        return true;
    }

}
