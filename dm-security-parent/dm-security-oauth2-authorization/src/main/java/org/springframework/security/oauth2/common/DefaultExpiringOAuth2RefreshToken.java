package org.springframework.security.oauth2.common;

import java.time.ZonedDateTime;

/**
 * <p>
 * See the <a href=
 * "https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Migration-Guide">OAuth
 * 2.0 Migration Guide</a> for Spring Security 5.
 *
 * @author Ryan Heaton
 */
public class DefaultExpiringOAuth2RefreshToken extends DefaultOAuth2RefreshToken implements ExpiringOAuth2RefreshToken {

    private static final long serialVersionUID = 3449554332764129719L;

    private final ZonedDateTime expiration;

    /**
     * @param value
     */
    public DefaultExpiringOAuth2RefreshToken(String value, ZonedDateTime expiration) {
        super(value);
        this.expiration = expiration;
    }

    /**
     * The instant the token expires.
     * 
     * @return The instant the token expires.
     */
    @Override
    public ZonedDateTime getExpiration() {
        return expiration;
    }

}
