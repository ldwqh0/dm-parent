package com.dm.wx.oauth2.authentication;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class WxAuthenticationToken extends AbstractAuthenticationToken implements Serializable {

    private final UserDetails userDetails;

    private final String code;

    public WxAuthenticationToken(
            final UserDetails userDetails,
            final String code,
            final Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.code = code;
        this.userDetails = userDetails;
        setAuthenticated(true);
    }

    private static final long serialVersionUID = -8573831337149756905L;

    @Override
    public Object getCredentials() {
        return code;
    }

    @Override
    public Object getPrincipal() {
        return userDetails;
    }

}
