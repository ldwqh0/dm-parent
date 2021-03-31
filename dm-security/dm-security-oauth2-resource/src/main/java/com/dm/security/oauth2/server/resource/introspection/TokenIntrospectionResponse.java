package com.dm.security.oauth2.server.resource.introspection;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionClaimNames.*;

public class TokenIntrospectionResponse extends HashMap<String, Object> {

    private static final long serialVersionUID = 9013023971620934272L;

    public TokenIntrospectionResponse(Map<String, ?> map) {
        this.putAll(map);
    }

    public boolean isActive() {
        return (boolean) get(ACTIVE);
    }

    public Object getSubject() {
        return get(SUBJECT);
    }

    public void setSubject(Object subject) {
        put(SUBJECT, subject);
    }

    public void setActive(boolean active) {
        put(ACTIVE, active);
    }

    public String getScope() {
        return (String) get(SCOPE);
    }

    public void setScope(String scope) {
        put(SCOPE, scope);
    }

    @JsonGetter("client_id")
    public String getClientId() {
        return (String) get(CLIENT_ID);
    }

    public void setClientId(String clientId) {
        put(CLIENT_ID, clientId);
    }

    public String getUsername() {
        return (String) get(USERNAME);
    }

    public void setUsername(String username) {
        put(USERNAME, username);
    }

    @JsonGetter("token_type")
    public String getTokenType() {
        return (String) get(TOKEN_TYPE);
    }

    public void setTokenType(String tokenType) {
        put(TOKEN_TYPE, tokenType);
    }

    @JsonGetter("exp")
    public Instant getExpiresAt() {
        return (Instant) get(EXPIRES_AT);
    }

    public void setExpiresAt(Instant expiresAt) {
        if (Objects.isNull(expiresAt)) {
            remove(EXPIRES_AT);
        } else {
            put(EXPIRES_AT, expiresAt);
        }
    }

    @JsonGetter("iat")
    public Instant getIssuedAt() {
        return (Instant) get(ISSUED_AT);
    }

    public void setIssuedAt(Instant issuedAt) {
        if (Objects.isNull(issuedAt)) {
            remove(ISSUED_AT);
        } else {
            put(ISSUED_AT, issuedAt);
        }
    }

    @JsonGetter("nbf")
    public Instant getNotBefore() {
        return (Instant) get(NOT_BEFORE);
    }

    public void setNotBefore(Instant notBefore) {
        if (Objects.isNull(notBefore)) {
            remove(NOT_BEFORE);
        } else {
            put(NOT_BEFORE, notBefore);
        }
    }

}
