package com.dm.security.web.authentication;

import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;

public class DelegateLogoutSuccessEntry {
    private final ServerWebExchangeMatcher matcher;
    private final ServerLogoutSuccessHandler sucessHandler;

    public DelegateLogoutSuccessEntry(ServerWebExchangeMatcher matcher, ServerLogoutSuccessHandler sucessHandler) {
        super();
        this.matcher = matcher;
        this.sucessHandler = sucessHandler;
    }

    public ServerWebExchangeMatcher getMatcher() {
        return this.matcher;
    }

    public ServerLogoutSuccessHandler getLogoutSuccessHandler() {
        return this.sucessHandler;
    }

}
