package com.dm.server.authorization.oauth2;

import com.dm.security.core.userdetails.UserDetailsDto;
import com.dm.security.oauth2.core.OAuth2UserDetailsDto;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.util.Assert;
import org.xyyh.authorization.core.OAuth2Authentication;
import org.xyyh.authorization.core.OAuth2ResourceServerTokenService;
import org.xyyh.authorization.core.OAuth2ServerAccessToken;

import java.time.Instant;
import java.util.*;

import static org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionClaimNames.*;


/**
 * <p>专用于授权服务器的token内省器</p>
 *
 * @author ldwqh0@outlook.com
 */
public class ServerOpaqueTokenIntrospector implements OpaqueTokenIntrospector, InitializingBean {

    private OAuth2ResourceServerTokenService accessTokenService = null;

    public void setAccessTokenService(OAuth2ResourceServerTokenService tokenService) {
        this.accessTokenService = tokenService;
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        OAuth2UserDetailsDto result = new OAuth2UserDetailsDto();
        Assert.notNull(accessTokenService, "the accessTokenService can not be null");
        OAuth2ServerAccessToken storedToken = accessTokenService.readAccessToken(token)
            .orElseThrow(() -> new BadOpaqueTokenException("The token [" + token + "] is not exist"));
        OAuth2Authentication authentication = accessTokenService.loadAuthentication(token)
            .orElseThrow(() -> new BadOpaqueTokenException("The authentication with token [" + token + "] is not exist"));
        Map<String, Object> attributes = new HashMap<>();
        Instant expiresAt = storedToken.getExpiresAt();
        Instant issuedAt = storedToken.getIssuedAt();
        Set<String> scopes = storedToken.getScopes();
        TokenType tokenType = storedToken.getTokenType();
        attributes.put(ACTIVE, Boolean.TRUE);
        attributes.put(EXPIRES_AT, expiresAt);
        attributes.put(ISSUED_AT, issuedAt);
        attributes.put(SCOPE, scopes);
        attributes.put(TOKEN_TYPE, tokenType);
        attributes.put(CLIENT_ID, Objects.requireNonNull(authentication).getClient().getClientId());
        attributes.put(NOT_BEFORE, issuedAt);
        result.setClientId(authentication.getClient().getClientId());
        result.setScopes(authentication.getScopes());
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            String username = userDetails.getUsername();
            result.setUsername(username);
            attributes.put(USERNAME, username);
            result.setGrantedAuthority(userDetails.getAuthorities());
        }
        if (principal instanceof UserDetailsDto) {
            UserDetailsDto userDetailsDto = (UserDetailsDto) principal;
            Long userid = userDetailsDto.getId();
            attributes.put(SUBJECT, userid);
            result.setId(userid);
        }
        result.setAttributes(Collections.unmodifiableMap(attributes));
        return result;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(accessTokenService, "the accessTokenService can not be null");
    }
}
