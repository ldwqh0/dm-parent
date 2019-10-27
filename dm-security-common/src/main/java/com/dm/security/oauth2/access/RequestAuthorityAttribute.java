package com.dm.security.oauth2.access;

import java.util.Set;

import org.apache.commons.collections4.SetUtils;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class RequestAuthorityAttribute {
    private final RequestMatcher requestMatcher;

    private final String authority;
    private final Set<String> scope;
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
