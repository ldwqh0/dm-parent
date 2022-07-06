package com.dm.wx.userdetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static com.dm.collections.Lists.arrayList;
import static java.util.Collections.unmodifiableCollection;

public class SimpleWxUserDetails implements WxUserDetails, UserDetails {

    private static final long serialVersionUID = 874394269828106471L;
    private final Collection<? extends GrantedAuthority> authorities;

//    private final WxMpUser wxMpUser;

    private final String openid;

//    private final String unionId;

    public SimpleWxUserDetails(String openid, Collection<? extends GrantedAuthority> authorities) {
        this.openid = openid;
//        this.openid = userinfo.getOpenid();
//        this.wxMpUser = userinfo;
//        this.unionId = userinfo.getUnionId();
        this.authorities = arrayList(authorities);
    }

//    public WxMpUser getWxMpUser() {
//        return wxMpUser;
//    }

    @Override
    public String getUnionid() {
        throw new RuntimeException("方法未实现");
    }

    @Override
    public String getOpenid() {
        return this.openid;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return unmodifiableCollection(authorities);
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return "N/A";
    }

    @Override
    public String getUsername() {
        return this.getOpenid();
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
