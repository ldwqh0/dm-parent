package com.dm.springboot.autoconfigure.security;

import java.util.Properties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.dm.security.verification.VerificationCodeGenerator;
import com.dm.security.verification.VerificationCodeStorage;
import com.dm.security.verification.controller.VerificationCodeController;
import com.dm.security.verification.support.InMemeryVerificationCodeStorage;
import com.dm.security.verification.support.SimpleVerificationCodeGenerator;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

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

    /**
     * 验证码生成器
     * 
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public Producer producer() {
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        kaptcha.setConfig(kaptchaConfig());
        return kaptcha;
    }

    private Config kaptchaConfig() {
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", "yes");
        properties.setProperty("kaptcha.image.width", "150");
        properties.setProperty("kaptcha.image.height", "50");
        return new Config(properties);
    }

}
