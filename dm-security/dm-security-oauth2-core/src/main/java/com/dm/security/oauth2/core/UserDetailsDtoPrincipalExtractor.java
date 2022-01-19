package com.dm.security.oauth2.core;

import com.dm.collections.Maps;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserDetailsDtoPrincipalExtractor implements PrincipalExtractor {

    @Override
    public OAuth2UserDetailsDto extract(Map<String, Object> map) {
        List<GrantedAuthority> authorities;
        if (Objects.nonNull(map.get("roles"))) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> roles = (List<Map<String, Object>>) map.get("roles");
            authorities = roles.stream()
                .map(role -> role.get("authority").toString())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        } else {
            authorities = Collections.emptyList();
        }
        // TODO clientId,scopes怎么处理
        OAuth2UserDetailsDto userDetailsDto = new OAuth2UserDetailsDto(null,
            null,
            Maps.getLong(map, StandardClaimNames.SUB),
            Maps.getString(map, StandardClaimNames.PREFERRED_USERNAME),
            authorities,
            Maps.getString(map, StandardClaimNames.NAME),
            null,
            null,
            (String) map.get("regionCode"),
            (String) map.get("scenicName"),
            map
        );
        return userDetailsDto;
    }

}
