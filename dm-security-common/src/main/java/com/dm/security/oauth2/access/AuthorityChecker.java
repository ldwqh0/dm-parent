package com.dm.security.oauth2.access;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * 一个权限校验器，提供一个简易的，遍历式的权限校验方法
 * 
 * @author ldwqh0@outlook.com
 * 
 * @since 0.2.1
 * 
 */
public class AuthorityChecker {

    private RequestAuthorityService authorityService;

    public void setAuthorityService(RequestAuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    /**
     * 判断指定的授权对象是否可以访问指定的请求<br>
     * 如果经过计算返回为true,表示可以访问<br>
     * 如果返回false表示拒绝访问
     * 
     * @param authentication 授权对象
     * @param request        要访问的请求
     * @return
     */
    public boolean check(Authentication authentication, HttpServletRequest request) {
        // 获取所有的授权对象
        List<RequestAuthorityAttribute> attributes = authorityService.listAllAuthorityAttribute();
        // 获取要检查的授权的角色
        Set<String> authToCheck = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .filter(StringUtils::isNotEmpty).collect(Collectors.toSet());
        List<RequestAuthorityAttribute> a = attributes.stream()
                .filter(attribute -> attribute.getRequestMatcher().matches(request))
                .filter(attribute -> authToCheck.contains(attribute.getAuthority()))
                .collect(Collectors.toList());
        // 投票表决
        int grantCount = 0;
        for (RequestAuthorityAttribute attribute : a) {
            // 如果当前配置为允许访问
            if (attribute.isAccess()) {
                // 如果Scope验证通过，投票+1
                if (validScope(attribute, authentication)) {
                    grantCount++;
                }
                // 如果有人反对，立即返回
            } else {
                return false;
            }
        }
        return grantCount > 0;
    }

    /**
     * 验证当前的授权对象是否允许访问指定资源scope的权限
     * 
     * @param attribute      要验证的对象
     * @param authentication 当前对象
     * @return
     */
    private boolean validScope(RequestAuthorityAttribute attribute, Authentication authentication) {
        if (authentication instanceof OAuth2Authentication) {
            Set<String> resourceScope = attribute.getScope();
            Set<String> requestScopes = ((OAuth2Authentication) authentication).getOAuth2Request().getScope();
            if (CollectionUtils.isNotEmpty(resourceScope)) {
                return CollectionUtils.containsAny(requestScopes, resourceScope);
            }
        }
        return true;
    }

}
