package org.springframework.security.oauth2.common;

/**
 * OpenID token
 */
public interface OpenIDAccessToken extends OAuth2AccessToken {
    String ID_TOKEN = "id_token";

    /**
     * idToken属性
     *
     * @return
     */
    String getIdToken();
}
