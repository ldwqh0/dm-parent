package com.dm.security.web.verification;

import com.dm.collections.CollectionUtils;
import com.dm.security.verification.DeviceVerificationCodeService;
import com.dm.security.web.authentication.AuthenticationObjectMapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 第三方工具验证码认证，包括手机，邮箱等工具的验证码认证
 *
 * @author LiDong
 */
public class DeviceVerificationCodeFilter extends GenericFilterBean {

    private final List<RequestMatcher> requestMatchers = new ArrayList<>();

    private String verifyIdParameterName = "verifyId";

    private String verifyCodeParameterName = "verifyCode";

    private String verifyCodeKeyParameterName = "email";

    private DeviceVerificationCodeService codeService = null;

    private ObjectMapper objectMapper = AuthenticationObjectMapperFactory.getObjectMapper();

    public void requestMatcher(RequestMatcher requestMatcher) {
        this.requestMatchers.add(requestMatcher);
    }

    public void setVerifyCodeKeyParameterName(String verifyCodeKeyParameterName) {
        this.verifyCodeKeyParameterName = verifyCodeKeyParameterName;
    }

    public void setVerifyCodeParameterName(String verifyCodeParameterName) {
        this.verifyCodeParameterName = verifyCodeParameterName;
    }

    public void setVerifyIdParameterName(String verifyIdParameterName) {
        this.verifyIdParameterName = verifyIdParameterName;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setCodeService(DeviceVerificationCodeService codeService) {
        this.codeService = codeService;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        if (requiresValidation(request)) {
            String verifyId = req.getParameter(verifyIdParameterName);
            String verifyCode = req.getParameter(verifyCodeParameterName);
            String key = req.getParameter(verifyCodeKeyParameterName);
            if (StringUtils.isNotBlank(verifyId) &&
                StringUtils.isNotBlank(key) &&
                StringUtils.isNotBlank(verifyCode) &&
                codeService.validate(verifyId, key, verifyCode)) {
                String requestKey = "DeviceVerificationCode.key";
                request.setAttribute(requestKey, key);
                chain.doFilter(req, res);
            } else {
                Map<String, Object> result = new HashMap<>();
                result.put("path", request.getRequestURI());
                result.put("error", HttpStatus.FORBIDDEN.getReasonPhrase());
                result.put("message", "验证码输入错误");
                result.put("status", HttpStatus.FORBIDDEN.value());
                result.put("timestamp", ZonedDateTime.now());
                response.setStatus(403);
                response.setContentType("application/json;charset=UTF-8");
                // 给出错误的提示信息
                response.getWriter().println(objectMapper.writeValueAsString(result));
            }
        } else {
            chain.doFilter(req, res);
        }
    }

    private boolean requiresValidation(HttpServletRequest request) {
        if (CollectionUtils.isNotEmpty(requestMatchers)) {
            for (RequestMatcher matcher : requestMatchers) {
                if (matcher.matches(request)) {
                    return true;
                }
            }
        }
        return false;
    }
}
