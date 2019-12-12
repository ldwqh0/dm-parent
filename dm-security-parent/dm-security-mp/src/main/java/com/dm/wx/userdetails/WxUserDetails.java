package com.dm.wx.userdetails;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class WxUserDetails implements UserDetails {

    private static final long serialVersionUID = 874394269828106471L;
    private Collection<? extends GrantedAuthority> authorities;
    private String username;
    private String openId;
    /**
     * 密码仅仅作为测试使用的
     */
    private String password;

    public WxUserDetails(String openId, Collection<? extends GrantedAuthority> authorities) {
        this.openId = openId;
        this.username = openId;
        this.authorities = authorities;
    }

    /**
     * 这个方法仅仅作为测试用
     * 
     * @param openId
     * @param password
     * @param authorities
     */
    @Deprecated
    public WxUserDetails(String openId, String password, String... authorities) {
        this.openId = openId;
        this.password = password;
        this.username = openId;
        this.authorities = AuthorityUtils.createAuthorityList(authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getOpenId() {
        return openId;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
