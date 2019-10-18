package com.dm.wx.oauth2.provider;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;

/**
 * 微信公众号用户Token包装
 * 
 * @author LiDong
 *
 */
public class WxMpOAuth2AuthenticationAccessToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 3880354314772820664L;

	private WxMpOAuth2AccessToken token;

	private WxUserDetails userDetails;

	public WxMpOAuth2AuthenticationAccessToken(WxMpOAuth2AccessToken token) {
		this(new WxUserDetails(token.getOpenId(), Collections.singleton(new SimpleGrantedAuthority("ROLE_WX_USER"))));
		this.token = token;
	}

	private WxMpOAuth2AuthenticationAccessToken(WxUserDetails userdetails) {
		this(userdetails, userdetails.getAuthorities());
	}

	private WxMpOAuth2AuthenticationAccessToken(WxUserDetails userDetails,
			Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.userDetails = userDetails;
		setDetails(userDetails);
		setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return token;
	}

	@Override
	public Object getPrincipal() {
		return userDetails;
	}

}
