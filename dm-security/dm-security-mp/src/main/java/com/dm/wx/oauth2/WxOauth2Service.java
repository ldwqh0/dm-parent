package com.dm.wx.oauth2;

public interface WxOauth2Service {
    WxOAuth2AccessToken getAccessToken(String appid, String code);
}
