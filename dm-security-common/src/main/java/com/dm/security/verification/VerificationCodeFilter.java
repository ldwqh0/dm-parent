package com.dm.security.verification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

public class VerificationCodeFilter extends GenericFilterBean {

	private final List<RequestMatcher> requestMathcers = new ArrayList<>();

	private VerificationCodeStorage storage;

	private String verifyIdParameterName = "verifyId";

	private String verifyCodeParameterName = "verifyCode";

	public void requestMatcher(RequestMatcher requestMatcher) {
		this.requestMathcers.add(requestMatcher);
	}

	public void setVerifyCodeService(VerificationCodeStorage storage) {
		this.storage = storage;
	}

	public void setVerifyCodeParameterName(String verifyCodeParameterName) {
		this.verifyCodeParameterName = verifyCodeParameterName;
	}

	public void setVerifyIdParameterName(String verifyIdParameterName) {
		this.verifyIdParameterName = verifyIdParameterName;
	}

	@Autowired
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
			if (storage.validate(verifyId, verifyCode)) {
				chain.doFilter(req, res);
			} else {
				// 给出错误的提示信息
				response.setStatus(403);
				response.getWriter().println("{\"error\":\"The parameter [" + verifyIdParameterName + "] and ["
						+ verifyCodeParameterName + "] is invalidate\"}");
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
		for (RequestMatcher matcher : requestMathcers) {
			if (matcher.matches(request)) {
				return true;
			}
		}
		return false;
	}

}
