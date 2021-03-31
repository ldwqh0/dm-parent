package com.dm.security.web.authentication;

import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;

public class DelegateLogoutSuccessEntry {
    private final ServerWebExchangeMatcher matcher;
    private final ServerLogoutSuccessHandler successHandler;

    public DelegateLogoutSuccessEntry(ServerWebExchangeMatcher matcher, ServerLogoutSuccessHandler successHandler) {
        super();
        this.matcher = matcher;
        this.successHandler = successHandler;
    }

    public ServerWebExchangeMatcher getMatcher() {
        return this.matcher;
    }

    public ServerLogoutSuccessHandler getLogoutSuccessHandler() {
        return this.successHandler;
    }

}
