package com.dm.security.authentication;

import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;

public class DelegateLogoutSuccessEntry {
    private ServerWebExchangeMatcher matcher;
    private ServerLogoutSuccessHandler sucessHandler;

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
