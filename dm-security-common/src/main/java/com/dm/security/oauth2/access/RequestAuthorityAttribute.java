package com.dm.security.oauth2.access;

import java.util.Set;

import org.apache.commons.collections4.SetUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * 一组资源授权配置，包括资源的匹配器，资源的scope，一个${@link Authentication}以及该组合是否可以通过
 * 
 * @author ldwqh0@outlook.com
 * @since 0.2.1
 *
 */
public class RequestAuthorityAttribute {
    /**
     * 资源的匹配器
     */
    private final RequestMatcher requestMatcher;

    /**
     * 资源的范围
     */
    private final Set<String> scope;

    /**
     * 角色信息
     */
    private final String authority;

    /**
     * 是否允许当前角色访问当前资源
     */
    private final boolean access;

    public RequestAuthorityAttribute(RequestMatcher requestMatcher, String authority, Set<String> scope,
            boolean access) {
        super();
        this.requestMatcher = requestMatcher;
        this.authority = authority;
        this.scope = SetUtils.unmodifiableSet(scope);
        this.access = access;
    }

    public RequestMatcher getRequestMatcher() {
        return requestMatcher;
    }

    public String getAuthority() {
        return authority;
    }

    public boolean isAccess() {
        return access;
    }

    public Set<String> getScope() {
        return scope;
    }

}
