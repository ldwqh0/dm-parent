package com.dm.wx.oauth2.authentication;

import com.dm.wx.oauth2.provider.WxOAuth2CodeAuthenticationToken;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

/**
 * @author 根据微信
 */
public class WxCodeAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
    public static final String WX_LOGIN_CODE_STATE = "wx_login_code_state";
    public static final String WX_LOGIN_APPID = "wx_login_appid";

    public WxCodeAuthenticationProcessingFilter() {
        super("/login/wx");
    }

    public WxCodeAuthenticationProcessingFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    public WxCodeAuthenticationProcessingFilter(RequestMatcher matcher) {
        super(matcher);
    }

    public WxCodeAuthenticationProcessingFilter(AuthenticationManager authenticationManager) {
        super("/login/wx", authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException, IOException {
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        if (StringUtils.isBlank(code)) {
            senRedirect(request, response);
            return null;
        } else {
            Object savedState = request.getSession().getAttribute(WX_LOGIN_CODE_STATE);
            String appid = String.valueOf(request.getSession().getAttribute(WX_LOGIN_APPID));
            if (!Objects.equals(savedState, state)) {
                throw new StateValidationFailureException("the give state not equals to request state");
            }
            return this.getAuthenticationManager().authenticate(new WxOAuth2CodeAuthenticationToken(appid, code));
        }
    }

    private void senRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String appid = request.getParameter("appid");
        String state = RandomStringUtils.randomAlphabetic(6);
        session.setAttribute(WX_LOGIN_CODE_STATE, state);
        session.setAttribute(WX_LOGIN_APPID, appid);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://open.weixin.qq.com/connect/oauth2/authorize");
        builder.queryParam("appid", appid);
        builder.queryParam("redirect_uri", buildRedirectUri(request));
        builder.queryParam("response_type", "code");
        builder.queryParam("scope", "snsapi_base");
        builder.queryParam("state", state);
        builder.fragment("wechat_redirect");
        String redirect = builder.toUriString();
        response.sendRedirect(redirect);
    }

    public String buildRedirectUri(HttpServletRequest request) {
        String schema = request.getHeader("x-forwarded-proto");
        String host = request.getHeader("x-forwarded-host");
        if (StringUtils.isBlank(schema)) {
            schema = request.getScheme();
        }
        if (StringUtils.isBlank(host)) {
            host = request.getHeader("host");
        }
        return schema + "://" + host + request.getServletPath();
    }
}
