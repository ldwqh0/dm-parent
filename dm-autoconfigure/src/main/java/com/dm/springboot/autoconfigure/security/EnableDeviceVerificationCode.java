package com.dm.springboot.autoconfigure.security;

import com.dm.security.verification.DeviceVerificationCodeStorage;
import com.dm.security.verification.support.InMemoryDeviceVerificationCodeStorage;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 增加设备验证码
 *
 * @author ldwqh0@outlook.com
 * @since 0.2.3
 */
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = {java.lang.annotation.ElementType.TYPE})
@Configuration
@Import({DeviceVerificationCodeConfiguration.class})
public @interface EnableDeviceVerificationCode {


}
