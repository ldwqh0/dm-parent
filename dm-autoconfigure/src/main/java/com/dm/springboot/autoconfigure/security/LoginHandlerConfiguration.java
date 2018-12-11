package com.dm.springboot.autoconfigure.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.dm.security.web.authentication.LoginFailureHandler;
import com.dm.security.web.authentication.LoginSuccessHandler;

@Configuration
@ConditionalOnClass(LoginSuccessHandler.class)
public class LoginHandlerConfiguration {
	@ConditionalOnMissingBean(AuthenticationSuccessHandler.class)
	@Bean
	public AuthenticationSuccessHandler loginSuccessHandler() {
		return new LoginSuccessHandler();
	}

	@ConditionalOnMissingBean(AuthenticationFailureHandler.class)
	@Bean
	public AuthenticationFailureHandler loginFailureHandler() {
		return new LoginFailureHandler();
	}
}
