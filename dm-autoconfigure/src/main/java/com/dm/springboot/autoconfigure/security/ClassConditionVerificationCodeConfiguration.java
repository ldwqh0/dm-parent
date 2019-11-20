package com.dm.springboot.autoconfigure.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

import com.dm.security.verification.VerificationCodeFilter;

@ConditionalOnBean(VerificationCodeFilter.class)
@Configuration
public class ClassConditionVerificationCodeConfiguration {

}
