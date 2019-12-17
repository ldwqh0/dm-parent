package com.dm.security.oauth2.core;


import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import com.dm.security.core.userdetails.GrantedAuthorityDto;
import com.dm.security.oauth2.core.PrincipalExtractor;
import com.dm.security.oauth2.core.UserDetailsDtoOAuth2User;

public class UserDetailsDtoPrincipalExtractor implements PrincipalExtractor {

    @Override
    public OAuth2AuthenticatedPrincipal extract(Map<String, Object> map) {

        UserDetailsDtoOAuth2User userDetailsDto = new UserDetailsDtoOAuth2User();
        userDetailsDto.setId(((Integer) map.get("id")).longValue());
        userDetailsDto.setUsername((String) map.get("username"));
        userDetailsDto.setFullname((String) map.get("fullname"));
        userDetailsDto.setRegionCode((String) map.get("regionCode"));
        if (!Objects.isNull(map.get("roles"))) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> roles = (List<Map<String, Object>>) map.get("roles");
            List<GrantedAuthorityDto> authorities = roles.stream()
                    .map(role -> new GrantedAuthorityDto((String) role.get("authority"),
                            ((Integer) role.get("id")).longValue()))
                    .collect(Collectors.toList());
            userDetailsDto.setGrantedAuthority(authorities);
        }
        userDetailsDto.setScenicName((String) map.get("scenicName"));
        return userDetailsDto;
    }

}
