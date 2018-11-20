package com.dm.security.oauth2.resource;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;

import com.dm.security.core.userdetails.GrantedAuthorityDto;
import com.dm.security.core.userdetails.UserDetailsDto;

public class UserDetailsDtoPrincipalExtractor implements PrincipalExtractor {

	@Override
	public Object extractPrincipal(Map<String, Object> map) {
		UserDetailsDto userDetailsDto = new UserDetailsDto();
		userDetailsDto.setId(Long.valueOf(map.get("id").toString()));
		userDetailsDto.setUsername(map.get("username").toString());
		userDetailsDto.setFullname(map.get("fullname").toString());
		if (!Objects.isNull(map.get("roles"))) {
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> roles = (List<Map<String, Object>>) map.get("roles");
			List<GrantedAuthorityDto> authorities = roles.stream()
					.map(role -> new GrantedAuthorityDto(role.get("authority").toString(),
							Long.valueOf(role.get("id").toString())))
					.collect(Collectors.toList());
			userDetailsDto.setGrantedAuthority(authorities);
		}

		if (map.get("region") != null) {
			@SuppressWarnings({ "unchecked" })
			List<String> regions = (List<String>) map.get("region");
			userDetailsDto.setRegion(regions);
		}

		return userDetailsDto;
	}

}
