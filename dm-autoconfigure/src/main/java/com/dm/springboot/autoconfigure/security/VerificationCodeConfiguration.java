package com.dm.springboot.autoconfigure.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dm.security.verification.VerificationCodeFilter;
import com.dm.security.verification.VerificationCodeGenerator;
import com.dm.security.verification.VerificationCodeStorage;
import com.dm.security.verification.controller.VerificationCodeController;
import com.dm.security.verification.support.InMemeryVerificationCodeStorage;
import com.dm.security.verification.support.SimpleVerificationCodeGenerator;

@ConditionalOnBean(VerificationCodeFilter.class)
@Configuration
public class VerificationCodeConfiguration {

	@Bean
	@ConditionalOnMissingBean(VerificationCodeStorage.class)
	public VerificationCodeStorage verificationCodeStorage() {
		return new InMemeryVerificationCodeStorage();
	}

	@Bean
	@ConditionalOnMissingBean(VerificationCodeGenerator.class)
	public VerificationCodeGenerator verificationCodeGenerator() {
		return new SimpleVerificationCodeGenerator();
	}

	@Bean
	public VerificationCodeController c() {
		return new VerificationCodeController();
	}

}
