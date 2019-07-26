package com.dm.oauth2;

import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * 当我们将应用作为client接入Oauth时，会涉及到token复制问题<br >
 * 参见{@link org.springframework.cloud.security.oauth2.client.AccessTokenContextRelay}<br>
 * 
 * 这个机制可以方便的向下游服务器发送请求<br>
 * 
 * 在通常情况下(基于session scope的OAuth2ClientContext)，这个没有问题。<br>
 * 但在当前系统中，存在两种模式，一种是前端直接使用access_token访问访问请求，一种是基于会话（session）的机制访问。
 * 当使用直接携带access_token的情景时，我们可能不需要创建session,而是仅仅作为无状态服务使用。<br>
 * 这个时候，我们需要一个不依赖于session scope的OAuth2ClientContext。<br>
 * 而且需要一种切换机制在不同的状态下使用不同的OAuth2ClientContext
 * 
 * @author LiDong
 *
 */
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
