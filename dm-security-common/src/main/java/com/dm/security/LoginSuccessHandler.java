package com.dm.security;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LoginSuccessHandler implements AuthenticationSuccessHandler, InitializingBean {

	@Autowired(required = false)
	private ObjectMapper objectMapper;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		Object principal = authentication.getPrincipal();
		if (!Objects.isNull(principal)) {
			String result = objectMapper.writeValueAsString(principal);
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(result);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (Objects.isNull(objectMapper)) {
			objectMapper = new ObjectMapper();
		}
	}
}
