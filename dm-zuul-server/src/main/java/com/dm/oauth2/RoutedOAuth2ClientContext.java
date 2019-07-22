package com.dm.oauth2;

import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

//TODO 这个有什么用都搞忘了
public abstract class RoutedOAuth2ClientContext implements OAuth2ClientContext {

	@Override
	public OAuth2AccessToken getAccessToken() {
		return determineTargetContext().getAccessToken();
	}

	@Override
	public void setAccessToken(OAuth2AccessToken accessToken) {
		determineTargetContext().setAccessToken(accessToken);
	}

	@Override
	public AccessTokenRequest getAccessTokenRequest() {
		return determineTargetContext().getAccessTokenRequest();
	}

	@Override
	public void setPreservedState(String stateKey, Object preservedState) {
		determineTargetContext().setPreservedState(stateKey, preservedState);
	}

	@Override
	public Object removePreservedState(String stateKey) {
		return determineTargetContext().removePreservedState(stateKey);
	}

	protected abstract OAuth2ClientContext determineTargetContext();

}
