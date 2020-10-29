package com.dm.uap.dingtalk.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * 通过钉钉用户信息获取到的 AuthenticationToken
 * 
 * @author LiDong
 *
 */
public class DingTalkAuthCodeAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = -1002298305299878178L;
    private final Object principal;

    public DingTalkAuthCodeAuthenticationToken(Object principal) {
        super(null);
        this.principal = principal;
        super.setAuthenticated(false);
    }

    public DingTalkAuthCodeAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

}
