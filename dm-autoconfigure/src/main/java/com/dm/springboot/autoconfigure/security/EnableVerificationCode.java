package com.dm.springboot.autoconfigure.security;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 增加验证码的Bean注册
 *
 * @author ldwqh0@outlook.com
 * @since 0.2.3
 */
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = {java.lang.annotation.ElementType.TYPE})
@Configuration
@Import({VerificationCodeConfiguration.class})
public @interface EnableVerificationCode {

}
