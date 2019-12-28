package com.dm.springboot.autoconfigure.common;

import javax.servlet.Servlet;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

import com.dm.springboot.web.servlet.error.MessageDetailErrorAttributes;

@Configuration
@AutoConfigureBefore({ ErrorMvcAutoConfiguration.class })
@ConditionalOnClass(value = { Servlet.class, DispatcherServlet.class })
public class WebControllerAdvice {
    /**
     * 重新定义ErrorAttributes,使之可以包含详情
     * 
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(value = ErrorAttributes.class, search = SearchStrategy.CURRENT)
    public MessageDetailErrorAttributes errorAttributes() {
        return new MessageDetailErrorAttributes();
    }
}
