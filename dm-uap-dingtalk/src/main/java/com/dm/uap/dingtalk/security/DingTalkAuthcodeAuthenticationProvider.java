package com.dm.uap.dingtalk.security;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import com.dm.dingtalk.api.service.DingTalkService;

public class DingTalkAuthcodeAuthenticationProvider implements AuthenticationProvider, InitializingBean {

	private AuthenticationUserDetailsService<DingTalkAuthCodeAuthenticationToken> userDetailsService;

	@Autowired
	private List<DingTalkService> dingTalkService;

	public void setDingTalkService(List<DingTalkService> dingTalkService) {
		this.dingTalkService = dingTalkService;
	}

	public void setUserDetailsService(
			AuthenticationUserDetailsService<DingTalkAuthCodeAuthenticationToken> userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication,
				"DingTalkAuthcodeAuthenticationProvider only support DingTalkAuthCodeAuthenticationToken");
		// TODO 首先通过钉钉API获取钉钉信息
		// TODO 在通过本地方法获取用户在系统中的信息
		UserDetails uer = userDetailsService.loadUserDetails((DingTalkAuthCodeAuthenticationToken) authentication);
		// TODO 创建一个成功的认证

		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return DingTalkAuthCodeAuthenticationToken.class.isAssignableFrom(authentication);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(userDetailsService, "the userDetailsService can not be null");
	}
}
