package com.dm.security.access;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
		// 如果没有找到相匹配的信息，弃权
		if (CollectionUtils.isEmpty(attributes)) {
			return ACCESS_ABSTAIN;
		}

		if (authentication instanceof OAuth2Authentication) {
			Object principal = ((OAuth2Authentication) authentication).getPrincipal();
			Set<String> scopes = ((OAuth2Authentication) authentication).getOAuth2Request().getScope();
			System.out.print(scopes);
		}
		List<RequestAuthorityAttribute> rAttributes = attributes.stream()
				.map(attribute -> (RequestAuthorityAttribute) attribute)
				.filter(attribute -> !Objects.isNull(attribute.getAccessable()))
				.collect(Collectors.toList());
		int grantCount = 0;
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		for (ConfigAttribute _attribute : rAttributes) {
			RequestAuthorityAttribute attribute = (RequestAuthorityAttribute) _attribute;
			for (GrantedAuthority authority : authorities) {
				if (Objects.equals(authority.getAuthority(), attribute.getAuthority())) {
					Boolean accessable = attribute.getAccessable();
					if (!Objects.isNull(accessable)) {
						if (accessable) {
							grantCount++;
						} else {
							return ACCESS_DENIED;
						}
					}
				}
			}
		}
		// 如果过程中没有产生成功的投票，弃权
		if (grantCount == 0) {
			return ACCESS_ABSTAIN;
		}
		return grantCount > 0 ? ACCESS_GRANTED : ACCESS_DENIED;
	}

}
