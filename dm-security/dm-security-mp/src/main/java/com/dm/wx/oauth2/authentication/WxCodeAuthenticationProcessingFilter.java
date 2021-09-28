package com.dm.wx.oauth2.authentication;

import com.dm.wx.oauth2.provider.WxOAuth2CodeAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 根据微信
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
        throws AuthenticationException {
        String code = request.getParameter("code");
        return this.getAuthenticationManager().authenticate(new WxOAuth2CodeAuthentication(code));
    }
}
