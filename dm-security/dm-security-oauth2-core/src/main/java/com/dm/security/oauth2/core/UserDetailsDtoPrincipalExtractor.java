package com.dm.security.oauth2.core;

import com.dm.collections.Maps;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserDetailsDtoPrincipalExtractor implements PrincipalExtractor {

    @Override
    public OAuth2UserDetailsDto extract(Map<String, Object> map) {
        OAuth2UserDetailsDto userDetailsDto = new OAuth2UserDetailsDto();
        userDetailsDto.setId(Maps.getLong(map, StandardClaimNames.SUB));
        userDetailsDto.setUsername(Maps.getString(map, StandardClaimNames.PREFERRED_USERNAME));
        userDetailsDto.setFullName(Maps.getString(map, StandardClaimNames.NAME));
        userDetailsDto.setRegionCode((String) map.get("regionCode"));
        if (Objects.nonNull(map.get("roles"))) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> roles = (List<Map<String, Object>>) map.get("roles");
            List<GrantedAuthority> authorities = roles.stream()
                .map(role -> role.get("authority").toString())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
            userDetailsDto.setGrantedAuthority(authorities);
        }
        userDetailsDto.setScenicName((String) map.get("scenicName"));
        userDetailsDto.setAttributes(map);
        return userDetailsDto;
    }

}
