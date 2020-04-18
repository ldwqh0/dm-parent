package com.dm.springboot.autoconfigure.wx;

import javax.servlet.Servlet;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

import com.dm.wx.controller.WxController;

import me.chanjar.weixin.mp.api.WxMpService;

public class WxConfiguration {

    @Bean
    @ConditionalOnBean(WxMpService.class)
    @ConditionalOnClass({ Servlet.class, WxController.class })
    public WxController wxController(WxMpService mpservice) {
        WxController controller = new WxController();
        controller.setWxMpService(mpservice);
        return controller;
    }

}
