package com.dm.security.web.authentication;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.dm.security.authentication.ResourceAuthorityAttribute;
import com.dm.security.authentication.ResourceAuthorityService;

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
        List<ResourceAuthorityAttribute> attributes = authorityService.listAll();
        // 获取要检查的授权的角色
        Set<String> authToCheck = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .filter(StringUtils::isNotEmpty).collect(Collectors.toSet());
        List<ResourceAuthorityAttribute> a = attributes.stream()
                .filter(attribute -> matches(request, attribute))
//                .filter(attribute -> authToCheck.contains(attribute.getAuthority()))
                .collect(Collectors.toList());
        // 投票表决
        int grantCount = 0;
        for (ResourceAuthorityAttribute attribute : a) {
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
        return false;
    }

    private boolean matches(HttpServletRequest request, ResourceAuthorityAttribute attribute) {
        // TODO Auto-generated method stub
        return null;
    }
}
