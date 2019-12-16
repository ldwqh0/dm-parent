package com.dm.auth.security;

import java.io.IOException;
import java.time.ZonedDateTime;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TermOfValidityFilter extends OncePerRequestFilter {

	private String authorizationKey;

	private ZonedDateTime expirationDate = ZonedDateTime.now();

//	private int count = 0;

	public TermOfValidityFilter(String authorizationKey) {
		this.authorizationKey = authorizationKey;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (expirationDate.isBefore(ZonedDateTime.now())) {
			request.getRequestDispatcher("/TermOfValidity").forward(request, response);
		} else {
			filterChain.doFilter(request, response);
		}
	}

	@Override
	protected void initFilterBean() throws ServletException {
		super.initFilterBean();
		try {
			String ts = AesUtil.decrypt(authorizationKey, "我都不知道我要输入一段什么样的文本来加密这个内容");
			this.expirationDate = ZonedDateTime.parse(ts);
		} catch (Exception e) {
			log.error("读取授权信息出错", e);
		}
	}

}
