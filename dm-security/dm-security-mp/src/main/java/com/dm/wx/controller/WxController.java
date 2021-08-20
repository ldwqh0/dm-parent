package com.dm.wx.controller;

import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("wx")
public class WxController {

    private WxMpService wxMpService;

    public void setWxMpService(WxMpService wxMpService) {
        this.wxMpService = wxMpService;
    }

    /**
     * 获取配置签名
     *
     * @param url
     * @throws WxErrorException
     */
    @GetMapping("jsapiTicket")
    public WxJsapiSignature jsTicket(@RequestParam("url") String url) throws WxErrorException {
        return wxMpService.createJsapiSignature(url);
    }

    /**
     * 根据code获取当前用户的openId
     *
     * @param code
     * @return
     * @throws WxErrorException
     */
    @GetMapping("openId")
    public String getOpenId(@RequestParam("code") String code) throws WxErrorException {
        return wxMpService.getOAuth2Service().getAccessToken(code).getOpenId();
    }
}
