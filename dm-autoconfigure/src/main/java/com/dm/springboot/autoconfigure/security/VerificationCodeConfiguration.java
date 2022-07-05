package com.dm.springboot.autoconfigure.security;

import com.dm.security.verification.VerificationCodeGenerator;
import com.dm.security.verification.VerificationCodeStorage;
import com.dm.security.verification.support.InMemoryVerificationCodeStorage;
import com.dm.security.verification.support.SimpleVerificationCodeGenerator;
import com.dm.security.web.verification.controller.VerificationCodeController;
import com.dm.security.web.verification.controller.VerificationReactiveController;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.Properties;

public class VerificationCodeConfiguration {

    @Bean
    @ConditionalOnMissingBean(VerificationCodeStorage.class)
    public VerificationCodeStorage verificationCodeStorage() {
        return new InMemoryVerificationCodeStorage();
    }

    @Bean
    @ConditionalOnMissingBean(VerificationCodeGenerator.class)
    public VerificationCodeGenerator verificationCodeGenerator() {
        return new SimpleVerificationCodeGenerator();
    }

    /**
     * 当有servlet时，创建这个Bean
     */
    @Bean
    @ConditionalOnClass(name = {"javax.servlet.Servlet"})
    @ConditionalOnMissingBean(type = {"org.springframework.web.reactive.DispatcherHandler"})
    public VerificationCodeController verificationCodeController(Producer producer, VerificationCodeGenerator validateCodeGenerator, VerificationCodeStorage codeStorage) {
        return new VerificationCodeController(producer, validateCodeGenerator, codeStorage);
    }

    @Bean
    @ConditionalOnMissingBean({VerificationCodeController.class})
    @ConditionalOnClass(name = {"org.reactivestreams.Publisher",
        "org.springframework.web.reactive.DispatcherHandler"})
    public VerificationReactiveController verificationReactiveController(Producer producer, VerificationCodeGenerator validateCodeGenerator, VerificationCodeStorage codeStorage) {
        return new VerificationReactiveController(producer, validateCodeGenerator, codeStorage);
    }

    /**
     * 验证码生成器
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
