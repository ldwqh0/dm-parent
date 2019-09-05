package com.dm.springboot.autoconfigure.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Import(LoginHandlerConfiguration.class)
@ConditionalOnClass(UsernamePasswordAuthenticationFilter.class)
public class SecurityConfiguration {

	@Bean
	@ConditionalOnMissingBean(PasswordEncoder.class)
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@ConditionalOnMissingBean(AuthenticationEntryPoint.class)
	public AuthenticationEntryPoint entryPoint() {
		return new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
	}
}
