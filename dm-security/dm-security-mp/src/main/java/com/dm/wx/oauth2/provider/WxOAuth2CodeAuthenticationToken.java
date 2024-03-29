package com.dm.wx.oauth2.provider;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 微信公众号包装 Code
 *
 * @author LiDong
 */
public class WxOAuth2CodeAuthenticationToken implements Authentication {

    private static final long serialVersionUID = 4815555761809821333L;
    private final String appid;

    private final String code;

    public WxOAuth2CodeAuthenticationToken(String appid, String code) {
        this.appid = appid;
        this.code = code;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return code;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return appid;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    }

}
