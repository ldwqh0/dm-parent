package com.dm.springboot.autoconfigure.common;

import com.dm.springboot.web.servlet.error.MessageDetailErrorAttributes;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@Configuration
@AutoConfigureBefore({ErrorMvcAutoConfiguration.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class WebControllerAdvice {
    /**
     * 重新定义ErrorAttributes,使之可以包含详情
     */
    @Bean
    @ConditionalOnMissingBean(value = ErrorAttributes.class, search = SearchStrategy.CURRENT)
    public MessageDetailErrorAttributes errorAttributes() {
        return new MessageDetailErrorAttributes();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        String requestUri = "";
        if (request instanceof ServletWebRequest) {
            requestUri = ((ServletWebRequest) request).getRequest().getRequestURI();
        }
        List<Map<String, Object>> errors = new ArrayList<>();
        for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
            v.getExecutableParameters();
            Map<String, Object> error = new HashMap<>();
            error.put("defaultMessage", v.getMessage());
            error.put("field", v.getPropertyPath().toString());
            v.getExecutableParameters();
            errors.add(error);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("error", "Bad Request");
        result.put("path", requestUri);
        result.put("message", ex.getMessage());
        result.put("errors", errors);
        result.put("status", HttpStatus.BAD_REQUEST.value());
        result.put("timestamp", Instant.now());
        return result;
    }
}
