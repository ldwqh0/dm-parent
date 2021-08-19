package com.dm.security.web.servlet.util.matcher;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

public class NoBearerTokenRequestMatcher implements RequestMatcher {

    private BearerTokenResolver bearerTokenResolver;

    public NoBearerTokenRequestMatcher(BearerTokenResolver bearerTokenResolver) {
        this.bearerTokenResolver = bearerTokenResolver;
    }

    public NoBearerTokenRequestMatcher() {
        this(new DefaultBearerTokenResolver());
    }

    public void setBearerTokenResolver(BearerTokenResolver bearerTokenResolver) {
        this.bearerTokenResolver = bearerTokenResolver;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        try {
            return this.bearerTokenResolver.resolve(request) == null;
        } catch (OAuth2AuthenticationException ex) {
            return true;
        }
    }

}
