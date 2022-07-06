package com.dm.wx.provider;

import com.dm.wx.WxMpConfigService;
import com.dm.wx.WxMpInfo;

public class SignalWxMpConfigService implements WxMpConfigService {

    private final String appid;
    private final String secret;

    public SignalWxMpConfigService(String appid, String secret) {
        this.appid = appid;
        this.secret = secret;
    }

    @Override
    public WxMpInfo getWxMpInfo(String appid) {
        return new WxMpInfo() {
            @Override
            public String getAppid() {
                return appid;
            }

            @Override
            public String getSecret() {
                return secret;
            }
        };
    }
}
