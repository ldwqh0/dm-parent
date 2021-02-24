package com.dm.security.oauth2.server.resource.introspection;

import com.dm.security.oauth2.core.OAuth2UserDetailsDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException;

import java.util.*;
import java.util.stream.Collectors;


public class UserDetailsDtoClaimsSetConverter implements IntrospectorClaimsSetConverter {

    Logger logger = LoggerFactory.getLogger(UserDetailsDtoClaimsSetConverter.class);

    @Override
    public OAuth2AuthenticatedPrincipal convert(Map<String, Object> map) {
        if (isActive(map)) {
            OAuth2UserDetailsDto user = new OAuth2UserDetailsDto();
            user.setAttributes(map);
            TokenIntrospectionResponse response = new TokenIntrospectionResponse(map);
            user.setUsername(response.getUsername());
            List<GrantedAuthority> authorities = new ArrayList<>();
            String scopeStr = response.getScope();
            if (StringUtils.isNotBlank(scopeStr)) {
                Set<String> scopes = Arrays.stream(StringUtils.split(scopeStr)).collect(Collectors.toSet());
                user.setScopes(scopes);
                scopes.forEach(scope -> authorities.add(new SimpleGrantedAuthority("SCOPE_" + scope)));
            }
            Object authoritiesAttributes = response.get("authorities");
            if (authoritiesAttributes instanceof List) {
                ((List<?>) authoritiesAttributes).forEach(a -> {
                    if (a instanceof Map && ((Map) a).containsKey("authority")) {
                        authorities.add(new SimpleGrantedAuthority(String.valueOf(((Map) a).get("authority"))));
                    }
                });
            }
            user.setId(Long.valueOf((String) response.getSubject()));
            user.setEnabled(true);
            user.setClientId(response.getClientId());
            user.setGrantedAuthority(authorities);
            return user;
        } else {
            this.logger.trace("Did not validate token since it is inactive");
            throw new BadOpaqueTokenException("Provided token isn't active");
        }
    }

    boolean isActive(Map<String, Object> result) {
        Object active = result.get("active");
        return Boolean.TRUE.equals(active);
    }


}
