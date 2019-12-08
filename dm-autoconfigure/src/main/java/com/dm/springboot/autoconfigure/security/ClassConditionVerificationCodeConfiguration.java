package com.dm.springboot.autoconfigure.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

import com.dm.security.web.verification.VerificationCodeFilter;

@ConditionalOnBean(VerificationCodeFilter.class)
@Configuration
public class ClassConditionVerificationCodeConfiguration {

}
