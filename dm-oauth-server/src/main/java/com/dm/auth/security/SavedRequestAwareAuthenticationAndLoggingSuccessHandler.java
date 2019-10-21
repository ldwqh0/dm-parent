package com.dm.auth.security;

import java.io.IOException;
import java.time.ZonedDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.dm.uap.entity.LoginLog;
import com.dm.uap.service.LoginLogService;

public class SavedRequestAwareAuthenticationAndLoggingSuccessHandler
		extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	private LoginLogService loginLogService;

	public void setLoginLogService(LoginLogService loginLogService) {
		this.loginLogService = loginLogService;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		super.onAuthenticationSuccess(request, response, authentication);
		// 记录登录成功日志
		LoginLog log = new LoginLog();
		String remote = request.getRemoteHost();
		String loginName = request.getParameter("username");
		log.setLoginName(loginName);
		log.setIp(remote);
		log.setResult("SUCCESS");
		log.setTime(ZonedDateTime.now());
		log.setType("LOGIN");
		loginLogService.save(log);
	}
}