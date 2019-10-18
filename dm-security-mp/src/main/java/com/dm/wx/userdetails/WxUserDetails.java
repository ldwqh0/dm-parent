package com.dm.wx.userdetails;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class WxUserDetails implements UserDetails {

	private static final long serialVersionUID = 874394269828106471L;
	private Collection<? extends GrantedAuthority> authorities;
	private String username;
	private String openId;

	public WxUserDetails(String openId, Collection<? extends GrantedAuthority> authorities) {
		this.openId = openId;
		this.username = openId;
		this.authorities = authorities;
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
		throw new UnsupportedOperationException();
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		throw new UnsupportedOperationException();
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		throw new UnsupportedOperationException();
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		throw new UnsupportedOperationException();
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		throw new UnsupportedOperationException();
	}

}
