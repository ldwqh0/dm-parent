package com.dm.security.web.verification;

import com.dm.collections.CollectionUtils;
import com.dm.security.verification.VerificationCode;
import com.dm.security.verification.VerificationCodeStorage;
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
import java.util.*;

public class VerificationCodeFilter extends GenericFilterBean {

    private final List<RequestMatcher> requestMatchers = new ArrayList<>();

    private final VerificationCodeStorage storage;

    private final ObjectMapper objectMapper;

    private String verifyIdParameterName = "verifyId";

    private String verifyCodeParameterName = "verifyCode";

    public VerificationCodeFilter(VerificationCodeStorage storage, ObjectMapper objectMapper) {
        this.storage = storage;
        this.objectMapper = objectMapper;
    }

    public VerificationCodeFilter(VerificationCodeStorage storage) {
        this(storage, AuthenticationObjectMapperFactory.getObjectMapper());
    }

    public void requestMatcher(RequestMatcher requestMatcher) {
        this.requestMatchers.add(requestMatcher);
    }

    public void setVerifyCodeParameterName(String verifyCodeParameterName) {
        this.verifyCodeParameterName = verifyCodeParameterName;
    }

    public void setVerifyIdParameterName(String verifyIdParameterName) {
        this.verifyIdParameterName = verifyIdParameterName;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        if (requiresValidation(request)) {
            String verifyId = req.getParameter(verifyIdParameterName);
            String verifyCode = req.getParameter(verifyCodeParameterName);
            if (validate(verifyId, verifyCode)) {
                storage.remove(verifyId);
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

    /**
     * 是否需要进行验证码校验
     *
     * @param request
     * @param response
     * @return
     */
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

    private boolean validate(String verifyId, String verifyCode) {
        VerificationCode savedCode = storage.get(verifyId);
        return (Objects.nonNull(savedCode))
            && StringUtils.isNotBlank(savedCode.getCode())
            && StringUtils.equals(savedCode.getCode(), verifyCode);
    }
}
