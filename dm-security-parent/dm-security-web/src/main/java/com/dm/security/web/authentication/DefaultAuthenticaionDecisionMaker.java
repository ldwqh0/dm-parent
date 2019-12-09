package com.dm.security.web.authentication;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import com.dm.security.authentication.ResourceAuthorityAttribute;
import com.dm.security.authentication.ResourceAuthorityService;
import com.dm.security.authentication.UriResource;
import com.dm.security.authentication.UriResource.MatchType;

/**
 * 授权决策器
 * 
 * @author ldwqh0@outlook.com
 *
 */
public class DefaultAuthenticaionDecisionMaker implements AuthenticaionDecisionMaker {

    private ResourceAuthorityService authorityService;

    public void setAuthorityService(ResourceAuthorityService authorityService) {
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

            // 如果资源允许任何被授权的用户访问,并且用户不是匿名用户，投票+1
            if (attribute.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
                grantCount++;
            }

            // 匿名用户的可访问权限包含在显示的权限配置中
            if (CollectionUtils.isNotEmpty(currentAuthorities)
                    && CollectionUtils.isNotEmpty(attribute.getAccessAuthority())
                    && CollectionUtils.containsAny(currentAuthorities, attribute.getAccessAuthority())) {
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
        if (MatchType.ANT_PATH.equals(matchType)) {
            return new AntPathRequestMatcher(resource.getPath(), resource.getMethod(), true).matches(request);
        }
        if (MatchType.REGEXP.equals(matchType)) {
            return new RegexRequestMatcher(resource.getPath(), resource.getMethod(), true).matches(request);
        }
        return false;
    }

    /**
     * 验证当前的授权对象是否允许访问指定资源scope的权限
     * 
     * @param attribute      要验证的对象
     * @param authentication 当前对象
     * @return
     */
    private boolean validScope(ResourceAuthorityAttribute attribute, Authentication authentication) {
        // TODO 这里进行scope校验
        // if (authentication instanceof OAuth2Authentication) {
        // Set<String> resourceScope = attribute.getScope();
        // Set<String> requestScopes = ((OAuth2Authentication)
        // authentication).getOAuth2Request().getScope();
        // if (CollectionUtils.isNotEmpty(resourceScope)) {
        // return CollectionUtils.containsAny(requestScopes, resourceScope);
        // }
        // }
        return true;
    }
}
