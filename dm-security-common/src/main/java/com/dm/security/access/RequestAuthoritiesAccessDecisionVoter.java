package com.dm.security.access;

import java.util.Collection;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.FilterInvocation;

import com.dm.security.access.RequestAuthorityAttribute;

public class RequestAuthoritiesAccessDecisionVoter implements AccessDecisionVoter<FilterInvocation> {

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return attribute instanceof RequestAuthorityAttribute;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}

	@Override
	public int vote(Authentication authentication, FilterInvocation object, Collection<ConfigAttribute> attributes) {
		int grantCount = 0;
		Authentication userAuthentication;
		if (authentication instanceof OAuth2Authentication) {
			userAuthentication = ((OAuth2Authentication) authentication).getUserAuthentication();
		} else {
			userAuthentication = authentication;
		}

		Collection<? extends GrantedAuthority> authorities = userAuthentication.getAuthorities();
		if (CollectionUtils.isEmpty(attributes)) {
			return ACCESS_ABSTAIN;
		}

		for (ConfigAttribute _attribute : attributes) {
			RequestAuthorityAttribute attribute = (RequestAuthorityAttribute) _attribute;
			for (GrantedAuthority authority : authorities) {
				if (Objects.equals(authority.getAuthority(), attribute.getAuthority())) {
					if (attribute.isAccessable()) {
						grantCount++;
					} else {
						return ACCESS_DENIED;
					}
				}
			}
		}
		return grantCount > 0 ? ACCESS_GRANTED : ACCESS_DENIED;
	}

}
