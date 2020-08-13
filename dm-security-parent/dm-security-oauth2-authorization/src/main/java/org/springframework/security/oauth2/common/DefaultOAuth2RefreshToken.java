package org.springframework.security.oauth2.common;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * An OAuth 2 refresh token.
 *
 * <p>
 * See the <a href=
 * "https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Migration-Guide">OAuth
 * 2.0 Migration Guide</a> for Spring Security 5.
 *
 * @author Ryan Heaton
 * @author Dave Syer
 */
public class DefaultOAuth2RefreshToken implements Serializable, OAuth2RefreshToken {

    private static final long serialVersionUID = 8349970621900575838L;

    private String value;

    /**
     * Create a new refresh token.
     */
    @JsonCreator
    public DefaultOAuth2RefreshToken(String value) {
        this.value = value;
    }

    /**
     * Default constructor for JPA and other serialization tools.
     */
    @SuppressWarnings("unused")
    private DefaultOAuth2RefreshToken() {
        this(null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.security.oauth2.common.IFOO#getValue()
     */
    @Override
    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultOAuth2RefreshToken)) {
            return false;
        }

        DefaultOAuth2RefreshToken that = (DefaultOAuth2RefreshToken) o;

        if (value != null ? !value.equals(that.value) : that.value != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
