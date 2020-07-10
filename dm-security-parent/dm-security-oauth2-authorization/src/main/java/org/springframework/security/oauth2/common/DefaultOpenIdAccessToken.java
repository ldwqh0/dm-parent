package org.springframework.security.oauth2.common;

public class DefaultOpenIdAccessToken extends DefaultOAuth2AccessToken implements OpenIDAccessToken {
    private String idToken;

    public DefaultOpenIdAccessToken(String value) {
        super(value);
    }

    public DefaultOpenIdAccessToken(String idToken, OAuth2AccessToken token) {
        super(token);
        this.idToken = idToken;
    }

    @Override
    public String getIdToken() {
        return this.idToken;
    }


    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
