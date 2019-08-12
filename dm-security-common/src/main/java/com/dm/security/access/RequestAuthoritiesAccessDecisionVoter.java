package com.dm.security.access;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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

		List<RequestAuthorityAttribute> rAttributes = attributes.stream()
				.map(attribute -> (RequestAuthorityAttribute) attribute)
				.filter(attribute -> !Objects.isNull(attribute.getAccessable()))
				.collect(Collectors.toList());
		int grantCount = 0;
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		for (ConfigAttribute _attribute : rAttributes) {
			RequestAuthorityAttribute attribute = (RequestAuthorityAttribute) _attribute;
			for (GrantedAuthority authority : authorities) {
				String authStr = attribute.getAuthority();
				// 因为系统增加了ROLE_GROUP功能，在处理匿名用户的时候，匿名用户的角色组，在系统运行的过程中会被创建为"ROLE_ANONYMOUS"
				// 而我们在系统中配置的匿名角色组对应的Authority为"内置分组_ROLE_ANONYMOUS"
				// 我们可以有几种方式来解决冲突，一个是修改AnonymousAuthenticationFilter,使的运行的匿名用户的"Authority"自动附加上"内置分组_"前缀
				// 二是修改保存在持久层中，默认匿名角色的角色名，使之适配“ROLE_ANONYMOUS”
				// 这里使用第二种方案
				// 因此，我们需要一种机制来实现配置保存的角色组名和Spring Security运行过程中的默认的匿名用户的“Authority”进行一个映射
				authStr = StringUtils.replace("内置分组_ROLE_ANONYMOUS", "内置分组_ROLE_ANONYMOUS", "ROLE_ANONYMOUS");
				if (Objects.equals(authority.getAuthority(), authStr)) {
					Boolean accessable = attribute.getAccessable();
					if (!Objects.isNull(accessable)) {
						if (accessable && validScope(attribute, authentication)) {
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

	/**
	 * 验证Scope
	 * 
	 * @param attribute
	 * @param request
	 * @return
	 */
	private boolean validScope(RequestAuthorityAttribute attribute, Authentication authentication) {
		if (authentication instanceof OAuth2Authentication) {
			Set<String> resourceScope = attribute.getScope();
			Set<String> requestScopes = ((OAuth2Authentication) authentication).getOAuth2Request().getScope();
			if (CollectionUtils.isNotEmpty(resourceScope)) {
				return CollectionUtils.containsAny(requestScopes, resourceScope);
			}
		}
		return true;
	}

}
