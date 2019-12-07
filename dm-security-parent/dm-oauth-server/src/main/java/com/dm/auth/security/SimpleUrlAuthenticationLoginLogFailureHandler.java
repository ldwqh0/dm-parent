package com.dm.auth.security;

import java.io.IOException;
import java.time.ZonedDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.dm.uap.entity.LoginLog;
import com.dm.uap.service.LoginLogService;

public class SimpleUrlAuthenticationLoginLogFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Autowired
	private LoginLogService loginLogService;

	public void setLoginLogService(LoginLogService loginLogService) {
		this.loginLogService = loginLogService;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		super.onAuthenticationFailure(request, response, exception);
		// 记录失败的登录日志
		LoginLog log = new LoginLog();
		String remote = request.getRemoteHost();
		String loginName = request.getParameter("username");
		log.setLoginName(loginName);
		log.setIp(remote);
		log.setResult("FAILURE");
		log.setTime(ZonedDateTime.now());
		log.setType("LOGIN");
		loginLogService.save(log);
	}
}
