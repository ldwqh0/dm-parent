package com.dm.security.web.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.dm.security.web.RequestUtils.isJsonRequest;

public class LoginFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    public LoginFailureHandler() {
        this(AuthenticationObjectMapperFactory.getObjectMapper());
    }

    public LoginFailureHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        if (isJsonRequest(request)) {
            Map<String, Object> result = new HashMap<>();
            result.put("path", request.getRequestURI());
            result.put("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            result.put("message", exception.getMessage());
            result.put("status", HttpStatus.UNAUTHORIZED.value());
            result.put("timestamp", ZonedDateTime.now());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(result));
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }
}
