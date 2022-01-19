package com.dm.security.oauth2.server.resource.introspection;

import com.dm.security.oauth2.core.OAuth2UserDetailsDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionClaimNames.*;


public class UserDetailsDtoClaimsSetConverter implements IntrospectorClaimsSetConverter {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsDtoClaimsSetConverter.class);

    private static final String SCOPE_PREFIX = "SCOPE_";

    private static final String DEFAULT_AUTHORITIES_ATTRIBUTE_KEY = "authorities";
    private static final String DEFAULT_AUTHORITY_ATTRIBUTE_KEY = "authority";

    private String authoritiesAttributeKey = DEFAULT_AUTHORITIES_ATTRIBUTE_KEY;

    private String authorityAttributeKey = DEFAULT_AUTHORITY_ATTRIBUTE_KEY;

    public void setAuthoritiesAttributeKey(String authoritiesAttributeKey) {
        this.authoritiesAttributeKey = authoritiesAttributeKey;
    }

    public void setAuthorityAttributeKey(String authorityAttributeKey) {
        this.authorityAttributeKey = authorityAttributeKey;
    }

    @Override
    public OAuth2AuthenticatedPrincipal convert(Map<String, Object> map) {
        if (isActive(map)) {
            TokenIntrospectionResponse response = new TokenIntrospectionResponse(map);
            Map<String, Object> attributes = new HashMap<>(map);
            attributes.put(ISSUED_AT, toInstant(map.get(ISSUED_AT)));
            attributes.put(EXPIRES_AT, toInstant(map.get(EXPIRES_AT)));
            attributes.put(NOT_BEFORE, toInstant(map.get(NOT_BEFORE)));
            List<GrantedAuthority> authorities = new ArrayList<>();
            Set<String> scopes;
            String scopeStr = response.getScope();
            if (StringUtils.isNotBlank(scopeStr)) {
                scopes = Arrays.stream(StringUtils.split(scopeStr)).collect(Collectors.toSet());
                scopes.forEach(scope -> authorities.add(new SimpleGrantedAuthority(SCOPE_PREFIX + scope)));
            } else {
                scopes = Collections.emptySet();
            }
            Object authoritiesAttributes = response.get(authoritiesAttributeKey);
            if (authoritiesAttributes instanceof List) {
                ((List<?>) authoritiesAttributes).forEach(a -> {
                    if (a instanceof Map && ((Map) a).containsKey(authorityAttributeKey)) {
                        authorities.add(new SimpleGrantedAuthority(String.valueOf(((Map) a).get(authorityAttributeKey))));
                    }
                });
            }
            Long userid = null;
            String subString = String.valueOf(response.getSubject());
            if (NumberUtils.isCreatable(subString)) {
                userid = NumberUtils.createLong(subString);
            }
            return new OAuth2UserDetailsDto(
                response.getClientId(),
                scopes,
                userid,
                response.getUsername(),
                authorities,
                (String) response.getOrDefault("fullName", ""),
                null,
                null,
                null,
                null,
                attributes);

        } else {
            logger.trace("Did not validate token since it is inactive");
            throw new BadOpaqueTokenException("Provided token isn't active");
        }
    }

    private Long toLong(Object v) {
        if (Objects.isNull(v)) {
            return null;
        } else if (v instanceof Number) {
            return ((Number) v).longValue();
        } else {
            return Long.valueOf(String.valueOf(v));
        }
    }

    private boolean isActive(Map<String, Object> result) {
        Object active = result.get("active");
        return Boolean.TRUE.equals(active);
    }

    private Instant toInstant(Object v) {
        if (v == null) {
            return null;
        } else if (v instanceof Instant) {
            return (Instant) v;
        } else if (v instanceof Number) {
            return Instant.ofEpochSecond(((Number) v).longValue());
        } else {
            return null;
        }
    }
}
