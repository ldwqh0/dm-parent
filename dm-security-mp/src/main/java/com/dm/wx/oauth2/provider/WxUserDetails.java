package com.dm.wx.oauth2.provider;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class WxUserDetails implements UserDetails {

	private static final long serialVersionUID = 874394269828106471L;
	private Collection<? extends GrantedAuthority> authorities;
	private String username;

	public WxUserDetails(String username, Collection<? extends GrantedAuthority> authorities) {
		this.username = username;
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
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
