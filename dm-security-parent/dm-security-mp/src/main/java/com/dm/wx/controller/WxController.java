package com.dm.wx.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;

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
     * @return
     * @throws WxErrorException
     */
    @GetMapping("jsapiTicket")
//    @ApiOperation("获取签名")
    public WxJsapiSignature jsTicket(@RequestParam("url") String url) throws WxErrorException {
        return wxMpService.createJsapiSignature(url);
    }

    @GetMapping("openId")
//    @ApiOperation("获取openid")
    public String getOpenId(@RequestParam("code") String code) throws WxErrorException {
        return wxMpService.oauth2getAccessToken(code).getOpenId();
    }
}
