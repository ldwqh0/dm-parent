package com.dm.wx.oauth2.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.dm.wx.oauth2.provider.WxOAuth2CodeAuthentication;

/**
 * 
 * @author 根据微信
 *
 */
public class WxCodeAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    public WxCodeAuthenticationProcessingFilter() {
        super("/login/wx");
    }

    public WxCodeAuthenticationProcessingFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    public WxCodeAuthenticationProcessingFilter(RequestMatcher matcher) {
        super(matcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        String code = request.getParameter("code");
        return this.getAuthenticationManager().authenticate(new WxOAuth2CodeAuthentication(code));
    }
}
