package com.dm.wx.oauth2.provider;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;

import com.dm.wx.oauth2.authentication.WxAuthenticationToken;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;

public class WxOauth2AuthenticationProvider implements AuthenticationProvider,
		InitializingBean, MessageSourceAware {

	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

	private AuthenticationUserDetailsService<WxMpOAuth2AuthenticationAccessToken> authenticationUserDetailsService;

	private WxMpService wxMpService;

	private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

	public void setWxMpService(WxMpService wxMpService) {
		this.wxMpService = wxMpService;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messages = new MessageSourceAccessor(messageSource);
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (!supports(authentication.getClass())) {
			return null;
		}
		try {
			String code = authentication.getCredentials().toString();
			WxMpOAuth2AccessToken token = wxMpService.oauth2getAccessToken(code);
			if (authenticationUserDetailsService != null) {
				UserDetails userDetails = authenticationUserDetailsService
						.loadUserDetails(new WxMpOAuth2AuthenticationAccessToken(token));
				return new WxAuthenticationToken(userDetails, code,
						authoritiesMapper.mapAuthorities(userDetails.getAuthorities()));
			} else {
				return new WxMpOAuth2AuthenticationAccessToken(token);
			}
		} catch (WxErrorException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public boolean supports(final Class<?> authentication) {
		return WxOAuth2CodeAuthentication.class.isAssignableFrom(authentication);
	}

	public void setAuthenticationUserDetailsService(
			AuthenticationUserDetailsService<WxMpOAuth2AuthenticationAccessToken> authenticationUserDetailsService) {
		this.authenticationUserDetailsService = authenticationUserDetailsService;
	}

}
