package com.dm.wx.userdetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class WxUserDetails implements UserDetails {

    private static final long serialVersionUID = 874394269828106471L;
    private final Collection<? extends GrantedAuthority> authorities;

    private final WxMpUser userinfo;

    public WxUserDetails(WxMpUser userinfo, Collection<? extends GrantedAuthority> authorities) {
        this.userinfo = userinfo;
        this.authorities = authorities;
    }

    public WxMpUser getUserinfo() {
        return userinfo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getOpenId() {
        return this.userinfo.getOpenId();
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return "N/A";
    }

    @Override
    public String getUsername() {
        return this.userinfo.getOpenId();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

}
