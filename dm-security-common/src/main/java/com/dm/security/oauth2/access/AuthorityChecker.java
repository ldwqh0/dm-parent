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

public class AuthorityChecker {

    private RequestAuthorityService authorityService;

    public void setAuthorityService(RequestAuthorityService authorityService) {
        this.authorityService = authorityService;
    }

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
            if (attribute.isAccess()) {
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
     * 验证Scope
     * 
     * @param attribute
     * @param request
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
