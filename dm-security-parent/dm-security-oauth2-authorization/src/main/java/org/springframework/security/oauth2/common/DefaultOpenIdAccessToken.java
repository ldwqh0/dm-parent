package org.springframework.security.oauth2.common;

public class DefaultOpenIdAccessToken extends DefaultOAuth2AccessToken implements OpenIDAccessToken {
  private static final long serialVersionUID = 1289071229991803752L;
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
