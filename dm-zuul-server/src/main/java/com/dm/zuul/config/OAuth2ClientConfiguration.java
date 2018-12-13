package com.dm.zuul.config;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.RedirectStrategy;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableOAuth2Client
public class OAuth2ClientConfiguration {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private OAuth2ClientContextFilter oAuth2ClientContextFilter;

	@PostConstruct
	public void setFilter() {
		oAuth2ClientContextFilter.setRedirectStrategy(new RedirectStrategy() {
			@Override
			public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url)
					throws IOException {
				// 重新配置oauth2Context的重定向策略，不进行重定向，而是将重定向信息交由前端处理
				response.setStatus(HttpStatus.SEE_OTHER.value());
				TreeMap<String, String> parameterMap = new TreeMap<>();
				parameterMap.put("redirect_url", url);
				try (PrintWriter writer = response.getWriter()) {
					writer.write(objectMapper.writeValueAsString(parameterMap));
				}
			}
		});
	}
}
