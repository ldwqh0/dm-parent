package com.dm.wx.oauth2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class WxOAuth2AccessToken implements Serializable {

    private static final long serialVersionUID = -8297615419316670721L;

    private final String appid;

    private final String accessToken;

    private final int expiresIn;

    private final String refreshToken;

    private final String openid;

    private final String scope;

    /**
     * token获取实际
     */

    private final ZonedDateTime createdTime = ZonedDateTime.now();

    @JsonCreator
    public WxOAuth2AccessToken(@JsonProperty("appid") String appid,
                               @JsonProperty("access_token") String accessToken,
                               @JsonProperty("expires_in") int expiresIn,
                               @JsonProperty("refresh_token") String refreshToken,
                               @JsonProperty("openid") String openid,
                               @JsonProperty("scope") String scope) {
        this.appid = appid;
        this.expiresIn = expiresIn;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.openid = openid;
        this.scope = scope;
    }

    public String getAppid() {
        return appid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getOpenid() {
        return openid;
    }

    public String getScope() {
        return scope;
    }

    public ZonedDateTime getCreatedTime() {
        return createdTime;
    }

    /**
     * 过期时间
     *
     * @return
     */
    public ZonedDateTime getExpireAt() {
        return createdTime.plusSeconds(expiresIn);
    }

    public static WxOAuth2AccessToken from(String appid, WxOAuth2AccessToken token) {
        return new WxOAuth2AccessToken(
            appid,
            token.accessToken,
            token.expiresIn,
            token.refreshToken,
            token.openid,
            token.scope
        );
    }
}
