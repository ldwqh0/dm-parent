package com.dm.server.authorization.converter;

import com.dm.security.core.userdetails.UserDetailsDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.xyyh.authorization.core.OAuth2Authentication;
import org.xyyh.authorization.core.OAuth2ServerAccessToken;
import org.xyyh.authorization.endpoint.converter.DefaultAccessTokenConverter;

import java.util.Map;
import java.util.Objects;

import static org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionClaimNames.SUBJECT;

@Component
public class DmAccessTokenConverter extends DefaultAccessTokenConverter {
    @Override
    public Map<String, Object> toAccessTokenIntrospectionResponse(OAuth2ServerAccessToken token, OAuth2Authentication authentication) {
        Object principal = authentication.getPrincipal();
        Map<String, Object> response = super.toAccessTokenIntrospectionResponse(token, authentication);
        if (principal instanceof String) {
            response.put(SUBJECT, principal);
        } else if (principal instanceof UserDetailsDto) {
            Long userId = ((UserDetailsDto) principal).getId();
            if (Objects.nonNull(userId)) {
                response.put(SUBJECT, String.valueOf(userId));
            }
        } else if (principal instanceof UserDetails) {
            response.put(SUBJECT, ((UserDetails) principal).getUsername());
        }
        return response;
    }
}
