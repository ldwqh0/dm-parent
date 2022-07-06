package com.dm.wx.controller;

import com.dm.wx.oauth2.WxOauth2Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("wx")
public class WxController {

//    private WxMpService wxMpService;

    private WxOauth2Service wxOauth2Service;

    public WxController() {
    }

//    public WxController(WxMpService wxMpService) {
//        this.wxMpService = wxMpService;
//    }
//
//    public void setWxMpService(WxMpService wxMpService) {
//        this.wxMpService = wxMpService;
//    }

//    /**
//     * 获取配置签名
//     *
//     * @param url
//     * @throws WxErrorException
//     */
//    @RequestMapping("jsapiTicket")
//    public WxJsapiSignature jsTicket(@RequestParam("url") String url) throws WxErrorException {
//        return wxMpService.createJsapiSignature(url);
//    }

    /**
     * 根据code获取当前用户的openId
     *
     * @param code
     */
    @GetMapping("openid")
    public String getOpenId(
        @RequestParam("appid") String appid,
        @RequestParam("code") String code) {
        return wxOauth2Service.getAccessToken(appid, code).getOpenid();
    }

//    @GetMapping("config")
//    public Map<String, ?> getConfig() {
//        String appId = wxMpService.getWxMpConfigStorage().getAppId();
//        return Maps.entry("appId", appId).build();
//    }
}
