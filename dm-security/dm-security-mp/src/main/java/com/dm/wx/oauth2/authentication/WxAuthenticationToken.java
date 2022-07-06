package com.dm.wx.oauth2.authentication;

import com.dm.wx.oauth2.WxOAuth2AccessToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

public class WxAuthenticationToken extends AbstractAuthenticationToken implements Serializable {

    private final UserDetails userDetails;

    private final WxOAuth2AccessToken accessToken;

    public WxAuthenticationToken(
        final WxOAuth2AccessToken accessToken,
        final UserDetails userDetails,
        final Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.userDetails = userDetails;
        this.accessToken = accessToken;
        setAuthenticated(true);
    }

    private static final long serialVersionUID = -8573831337149756905L;

    @Override
    public Object getCredentials() {
        return "N/A";
    }

    @Override
    public Object getPrincipal() {
        return userDetails;
    }

    public WxOAuth2AccessToken getAccessToken() {
        return this.accessToken;
    }

}
