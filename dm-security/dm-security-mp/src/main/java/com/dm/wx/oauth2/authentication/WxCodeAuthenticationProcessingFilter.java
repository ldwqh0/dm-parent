package com.dm.wx.oauth2.authentication;

import com.dm.wx.oauth2.provider.WxOAuth2CodeAuthentication;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author 根据微信
 */
public class WxCodeAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
    public static final String WX_LOGIN_CODE_STATE = "wx_login_code_state";

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
        throws AuthenticationException, IOException {
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        if (StringUtils.isBlank(code)) {
            throw new CodeNotFoundException("can not find authorize code,authorize request must be required");
        }
        Object savedState = request.getSession().getAttribute(WX_LOGIN_CODE_STATE);
        if (!Objects.equals(savedState, state)) {
            throw new StateValidationFailureException("the give state not equals to request state");
        }
        return this.getAuthenticationManager().authenticate(new WxOAuth2CodeAuthentication(code));
    }
}
