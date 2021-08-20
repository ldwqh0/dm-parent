package com.dm.wx.userdetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class WxUserDetailsDto implements WxUserDetails, UserDetails {

    private static final long serialVersionUID = 874394269828106471L;
    private final Collection<? extends GrantedAuthority> authorities;

    private final WxMpUser wxMpUser;

    private final String openId;

    private final String unionId;


    public WxUserDetailsDto(WxMpUser userinfo, Collection<? extends GrantedAuthority> authorities) {
        this.openId = userinfo.getOpenId();
        this.wxMpUser = userinfo;
        this.unionId = userinfo.getUnionId();
        this.authorities = authorities;
    }

    public WxMpUser getWxMpUser() {
        return wxMpUser;
    }

    @Override
    public String getOpenId() {
        return this.openId;
    }


    @Override
    public String getUnionId() {
        return this.unionId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return "N/A";
    }

    @Override
    public String getUsername() {
        return this.wxMpUser.getOpenId();
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
