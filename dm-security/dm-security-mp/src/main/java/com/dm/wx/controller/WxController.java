package com.dm.wx.controller;

import com.dm.collections.Maps;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("wx")
public class WxController {

    private WxMpService wxMpService;

    public WxController() {
    }

    public WxController(WxMpService wxMpService) {
        this.wxMpService = wxMpService;
    }

    public void setWxMpService(WxMpService wxMpService) {
        this.wxMpService = wxMpService;
    }

    /**
     * 获取配置签名
     *
     * @param url
     * @throws WxErrorException
     */
    @RequestMapping("jsapiTicket")
    public WxJsapiSignature jsTicket(@RequestParam("url") String url) throws WxErrorException {
        return wxMpService.createJsapiSignature(url);
    }

    /**
     * 根据code获取当前用户的openId
     *
     * @param code
     * @throws WxErrorException
     */
    @GetMapping("openId")
    public String getOpenId(@RequestParam("code") String code) throws WxErrorException {
        return wxMpService.getOAuth2Service().getAccessToken(code).getOpenId();
    }

    @GetMapping("config")
    public Map<String, ?> getConfig() {
        String appId = wxMpService.getWxMpConfigStorage().getAppId();
        return Maps.entry("appId", appId).build();
    }
}
