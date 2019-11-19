package com.dm.security.web.authentication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LoginFailureHandler implements AuthenticationFailureHandler, InitializingBean {

    @Autowired(required = false)
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        Map<String, Object> result = new HashMap<>();
        result.put("error", HttpStatus.UNAUTHORIZED.value());
        result.put("message", exception.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (Objects.isNull(objectMapper)) {
            objectMapper = new ObjectMapper();
        }
    }

}
