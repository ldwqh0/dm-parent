package org.springframework.security.oauth2.common;

/**
 * OpenID token
 */
public interface OpenIDAccessToken extends OAuth2AccessToken {
    public static String ID_TOKEN = "id_token";

    /**
     * idToken属性
     *
     * @return
     */
    public String getIdToken();
}
