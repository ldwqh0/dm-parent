package com.dm.security.web.verification;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import com.dm.collections.CollectionUtils;
import com.dm.security.verification.VerificationCode;
import com.dm.security.verification.VerificationCodeStorage;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VerificationCodeFilter extends GenericFilterBean {

    private final List<RequestMatcher> requestMathcers = new ArrayList<>();

    @Autowired
    private VerificationCodeStorage storage = null;

    private ObjectMapper om = new ObjectMapper();

    private String verifyIdParameterName = "verifyId";

    private String verifyCodeParameterName = "verifyCode";

    public void requestMatcher(RequestMatcher requestMatcher) {
        this.requestMathcers.add(requestMatcher);
    }

    public void setVerifyCodeParameterName(String verifyCodeParameterName) {
        this.verifyCodeParameterName = verifyCodeParameterName;
    }

    public void setVerifyIdParameterName(String verifyIdParameterName) {
        this.verifyIdParameterName = verifyIdParameterName;
    }

    @Autowired(required = false)
    public void setObjectMapper(ObjectMapper om) {
        this.om = om;
    }

    public void setStorage(VerificationCodeStorage storage) {
        this.storage = storage;
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
                response.getWriter().println(om.writeValueAsString(result));
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
        if (CollectionUtils.isNotEmpty(requestMathcers)) {
            for (RequestMatcher matcher : requestMathcers) {
                if (matcher.matches(request)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        Assert.notNull(storage, "the storage can not be null");
        if (Objects.isNull(om)) {
            this.om = new ObjectMapper();
        }
    }

    private boolean validate(String verifyId, String verifyCode) {
        VerificationCode savedCode = storage.get(verifyId);
        return (Objects.nonNull(savedCode))
                && StringUtils.isNotBlank(savedCode.getCode())
                && StringUtils.equals(savedCode.getCode(), verifyCode);
    }
}
