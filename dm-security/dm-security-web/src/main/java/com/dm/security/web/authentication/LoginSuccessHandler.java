package com.dm.security.web.authentication;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import static com.dm.security.web.RequestUtils.*;

public class LoginSuccessHandler implements AuthenticationSuccessHandler, InitializingBean {

    @Autowired(required = false)
    private ObjectMapper objectMapper;

    public LoginSuccessHandler() {
        this(new ObjectMapper());
    }

    public LoginSuccessHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        // 是否重定向,如果参数中有redirect参数，系统会自动跳转到相应的地址
        String redirect = request.getParameter("redirect_uri");
        if (isJsonRequest(request)) {
            Object principal = authentication.getPrincipal();
            if (Objects.nonNull(principal)) {
                String result = objectMapper.writeValueAsString(principal);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(result);
            }
        } else {
            // TODO 重定向策略需要补充
            if (StringUtils.isNotBlank(redirect)) {
                response.sendRedirect(redirect);
            } else {
                response.sendRedirect("/");
            }
        }
    }

    @Override
    public void afterPropertiesSet() {
        if (Objects.isNull(objectMapper)) {
            objectMapper = new ObjectMapper();
        }
    }
}
