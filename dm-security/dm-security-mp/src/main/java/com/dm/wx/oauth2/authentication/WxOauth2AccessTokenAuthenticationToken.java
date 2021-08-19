package com.dm.wx.oauth2.authentication;

import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class WxOauth2AccessTokenAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 9069951558338657308L;

    private final WxOAuth2AccessToken accessToken;

    public WxOauth2AccessTokenAuthenticationToken(WxOAuth2AccessToken accessToken) {
        super(Collections.singletonList(new SimpleGrantedAuthority("ROLE_WX_USER")));
        this.accessToken = accessToken;
    }

    public WxOauth2AccessTokenAuthenticationToken(WxOAuth2AccessToken accessToken, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.accessToken = accessToken;
    }

    @Override
    public Object getCredentials() {
        return "N/A";
    }

    @Override
    public Object getPrincipal() {
        return accessToken;
    }
}
