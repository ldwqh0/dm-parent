package com.dm.security.access;

import java.util.Set;

import org.springframework.http.HttpMethod;
import org.springframework.security.access.ConfigAttribute;

public class RequestAuthorityAttribute implements ConfigAttribute {

	/**
	 * 资源的匹配模式
	 * 
	 * @author LiDong
	 *
	 */
	public enum MatchType {
		/**
		 * 路径匹配
		 */
		ANT_PATH,
		/**
		 * 正则表达式匹配
		 */
		REGEXP,
	}

	private static final long serialVersionUID = -5892097102006537218L;

	/**
	 * 授权角色
	 */
	private String authority;

	private String pattern;

	private MatchType matchType;

	private HttpMethod method;

	private Boolean accessable;

	private Set<String> scope;

	public RequestAuthorityAttribute(String authority, String pattern, MatchType matchType, Set<String> scope,
			HttpMethod method,
			Boolean accessable) {
		super();
		this.authority = authority;
		this.scope = scope;
		this.pattern = pattern;
		this.matchType = matchType;
		this.method = method;
		this.accessable = accessable;
	}

	@Override
	public String getAttribute() {
		return toString();
	}

	public RequestAuthorityAttribute() {
		super();
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getPattern() {
		return pattern;
	}

	public MatchType getMatchType() {
		return matchType;
	}

	public void setMatchType(MatchType matchType) {
		this.matchType = matchType;
	}

	public HttpMethod getMethod() {
		return method;
	}

	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public Boolean getAccessable() {
		return accessable;
	}

	public void setAccessable(Boolean accessable) {
		this.accessable = accessable;
	}

	public Set<String> getScope() {
		return scope;
	}

	public void setScope(Set<String> scope) {
		this.scope = scope;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessable == null) ? 0 : accessable.hashCode());
		result = prime * result + ((authority == null) ? 0 : authority.hashCode());
		result = prime * result + ((matchType == null) ? 0 : matchType.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime * result + ((pattern == null) ? 0 : pattern.hashCode());
		result = prime * result + ((scope == null) ? 0 : scope.hashCode());
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
		RequestAuthorityAttribute other = (RequestAuthorityAttribute) obj;
		if (accessable == null) {
			if (other.accessable != null)
				return false;
		} else if (!accessable.equals(other.accessable))
			return false;
		if (authority == null) {
			if (other.authority != null)
				return false;
		} else if (!authority.equals(other.authority))
			return false;
		if (matchType != other.matchType)
			return false;
		if (method != other.method)
			return false;
		if (pattern == null) {
			if (other.pattern != null)
				return false;
		} else if (!pattern.equals(other.pattern))
			return false;
		if (scope == null) {
			if (other.scope != null)
				return false;
		} else if (!scope.equals(other.scope))
			return false;
		return true;
	}

}
