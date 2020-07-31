package com.dm.security.oauth2.core;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class UserDetailsDtoPrincipalExtractor implements PrincipalExtractor {

  @Override
  public OAuth2User extract(Map<String, Object> map) {
    UserDetailsDtoOAuth2User userDetailsDto = new UserDetailsDtoOAuth2User();
    userDetailsDto.setId(((Integer) map.get("id")).longValue());
    userDetailsDto.setUsername((String) map.get("username"));
    userDetailsDto.setFullname((String) map.get("fullname"));
    userDetailsDto.setRegionCode((String) map.get("regionCode"));
    if (!Objects.isNull(map.get("roles"))) {
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
