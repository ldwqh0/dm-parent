package com.dm.security.oauth2.provider.token;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.util.StringUtils;

import com.dm.security.core.userdetails.UserDetailsDto;
import com.dm.security.oauth2.resource.UserDetailsDtoPrincipalExtractor;

/**
 * 一个 {@link UserAuthenticationConverter}实现<br />
 * 这个实现会解析 对象中的principal对象到{@link UserDetailsDto}
 * 
 * @author LiDong
 *
 */
public class UserDetailsAuthenticationConverter extends DefaultUserAuthenticationConverter {
	private static final String PRINCIPAL = "principal";

	private Collection<? extends GrantedAuthority> defaultAuthorities;

	private PrincipalExtractor principalExtractor = new UserDetailsDtoPrincipalExtractor();

	private UserDetailsService userDetailsService;

	@Override
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	public void setPrincipalExtractor(PrincipalExtractor principalExtractor) {
		this.principalExtractor = principalExtractor;
	}

	@Override
	public void setDefaultAuthorities(String[] defaultAuthorities) {
		this.defaultAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils
				.arrayToCommaDelimitedString(defaultAuthorities));
	}

	@Override
	public Map<String, ?> convertUserAuthentication(Authentication userAuthentication) {
		@SuppressWarnings("unchecked")
		Map<String, Object> response = (Map<String, Object>) super.convertUserAuthentication(userAuthentication);
		Object principal = userAuthentication.getPrincipal();
		if (principal instanceof UserDetailsDto) {
			response.put(PRINCIPAL, principal);
		}
		return response;
	}

	// 从map中解码用户权限
	@Override
	public Authentication extractAuthentication(Map<String, ?> map) {
		UsernamePasswordAuthenticationToken token = null;
		if (map.containsKey(USERNAME)) {
			Object principal = map.get(USERNAME);
			Collection<? extends GrantedAuthority> authorities = getAuthorities(map);
			if (userDetailsService != null) {
				UserDetails user = userDetailsService.loadUserByUsername((String) map.get(USERNAME));
				authorities = user.getAuthorities();
				principal = user;
			} else if (!Objects.isNull(principalExtractor) && map.containsKey(PRINCIPAL)) {
				@SuppressWarnings("unchecked")
				Map<String, Object> principalMap = (Map<String, Object>) map.get(PRINCIPAL);
				principal = principalExtractor.extractPrincipal(principalMap);
			}
			token = new UsernamePasswordAuthenticationToken(principal, "N/A", authorities);
		}
		return token;
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
		if (!map.containsKey(AUTHORITIES)) {
			return defaultAuthorities;
		}
		Object authorities = map.get(AUTHORITIES);
		if (authorities instanceof String) {
			return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
		}
		if (authorities instanceof Collection) {
			return AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils
					.collectionToCommaDelimitedString((Collection<?>) authorities));
		}
		throw new IllegalArgumentException("Authorities must be either a String or a Collection");
	}
}
