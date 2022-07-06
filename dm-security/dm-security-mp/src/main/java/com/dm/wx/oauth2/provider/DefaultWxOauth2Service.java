package com.dm.wx.oauth2.provider;

import com.dm.wx.WxMpConfigService;
import com.dm.wx.WxMpInfo;
import com.dm.wx.oauth2.WxOAuth2AccessToken;
import com.dm.wx.oauth2.WxOauth2Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestOperations;

public class DefaultWxOauth2Service implements WxOauth2Service {

    private final WxMpConfigService wxMpConfigService;

    private final RestOperations restOperations;

    public DefaultWxOauth2Service(WxMpConfigService wxMpConfigService, RestOperations restOperations, ObjectMapper objectMapper) {
        this.wxMpConfigService = wxMpConfigService;
        this.restOperations = restOperations;
        this.objectMapper = objectMapper;
    }

    private final ObjectMapper objectMapper;

    @Override
    public WxOAuth2AccessToken getAccessToken(String appid, String code) {
        try {
            WxMpInfo wxMpInfo = wxMpConfigService.getWxMpInfo(appid);
            String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={appid}&secret={secret}&code={code}&grant_type=authorization_code";
            String tokenStr = restOperations.getForObject(url, String.class, wxMpInfo.getAppid(), wxMpInfo.getSecret(), code);
            return objectMapper.readValue(tokenStr, WxOAuth2AccessToken.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

