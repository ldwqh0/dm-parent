package com.dm.security.access;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import com.dm.security.access.RequestAuthorityAttribute;
import com.dm.security.access.RequestAuthorityAttribute.MatchType;
import com.dm.security.provider.RequestAuthoritiesService;

public class RequestAuthoritiesFilterInvocationSecurityMetadataSource
		implements FilterInvocationSecurityMetadataSource {

	@Autowired
	private RequestAuthoritiesService requestAuthoritiesService;

	private Map<RequestInfo, RequestMatcher> requestMatchMap = new ConcurrentHashMap<RequestInfo, RequestMatcher>();

	public RequestAuthoritiesFilterInvocationSecurityMetadataSource() {
		super();
	}

	public RequestAuthoritiesFilterInvocationSecurityMetadataSource(
			RequestAuthoritiesService requestAuthoritiesService) {
		super();
		this.requestAuthoritiesService = requestAuthoritiesService;
	}

	@Autowired
	public void setRequestAuthoritiesService(RequestAuthoritiesService requestAuthoritiesService) {
		this.requestAuthoritiesService = requestAuthoritiesService;
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) {
		final HttpServletRequest request = ((FilterInvocation) object).getRequest();
		List<RequestAuthorityAttribute> allAttributes = requestAuthoritiesService.listAllAttributes();
		return allAttributes.stream().filter(attribute -> match(request, attribute)).collect(Collectors.toList());
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return Collections.emptySet();
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}

	private boolean match(HttpServletRequest request, RequestAuthorityAttribute attribute) {
		return getRequestMatcher(attribute).matches(request);
	}

	private RequestMatcher getRequestMatcher(RequestAuthorityAttribute attribute) {
		String pattern = attribute.getPattern();
		HttpMethod method = attribute.getMethod();
		MatchType matchType = attribute.getMatchType();
		RequestInfo requestInfo = new RequestInfo(pattern, method, matchType);
		RequestMatcher matcher = requestMatchMap.get(requestInfo);
		if (Objects.isNull(matcher)) {
			if (MatchType.ANT_PATH.equals(matchType)) {
				matcher = new AntPathRequestMatcher(pattern, method.toString());
			} else if (MatchType.REGEXP.equals(matchType)) {
				matcher = new RegexRequestMatcher(pattern, method.toString());
			}
			requestMatchMap.put(requestInfo, matcher);
		}
		return matcher;
	}
}

class RequestInfo {
	private String pattern;
	private HttpMethod method;
	private MatchType matchType;

	public RequestInfo(String pattern, HttpMethod method, MatchType matchType) {
		super();
		this.pattern = pattern;
		this.method = method;
		this.matchType = matchType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((matchType == null) ? 0 : matchType.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime * result + ((pattern == null) ? 0 : pattern.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RequestInfo other = (RequestInfo) obj;
		if (matchType != other.matchType)
			return false;
		if (method != other.method)
			return false;
		if (pattern == null) {
			if (other.pattern != null)
				return false;
		} else if (!pattern.equals(other.pattern))
			return false;
		return true;
	}

}
