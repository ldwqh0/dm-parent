package com.dm.uap.dingtalk.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DingTalkAuthcodeAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    public DingTalkAuthcodeAuthenticationProcessingFilter() {
        super(new AntPathRequestMatcher("/login/dingtalk", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        String authCode = request.getParameter("auth_code");
        log.debug("Get auth_code from parameter, auth_code=[" + authCode + "]");
        DingTalkAuthCodeAuthenticationToken token = new DingTalkAuthCodeAuthenticationToken(authCode);
        log.debug("create new AuthCodeAuthenticationToken [" + token + "]");
        return getAuthenticationManager().authenticate(token);
    }

}
