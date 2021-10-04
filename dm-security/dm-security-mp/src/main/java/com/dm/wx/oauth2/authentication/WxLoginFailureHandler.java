package com.dm.wx.oauth2.authentication;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.dm.wx.oauth2.authentication.WxCodeAuthenticationProcessingFilter.WX_LOGIN_CODE_STATE;

public class WxLoginFailureHandler implements AuthenticationFailureHandler {

    private final String appId;

    public WxLoginFailureHandler(String appId) {
        this.appId = appId;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (CodeNotFoundException.class.isAssignableFrom(exception.getClass())) {
            HttpSession session = request.getSession();
            String state = RandomStringUtils.randomAlphabetic(6);
            session.setAttribute(WX_LOGIN_CODE_STATE, state);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://open.weixin.qq.com/connect/oauth2/authorize");
            builder.queryParam("appid", appId);
            builder.queryParam("redirect_uri", buildRedirectUri(request));
            builder.queryParam("response_type", "code");
            builder.queryParam("scope", "snsapi_base");
            builder.queryParam("state", state);
            builder.fragment("wechat_redirect");
            String redirect = builder.toUriString();
            response.sendRedirect(redirect);
        }
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
