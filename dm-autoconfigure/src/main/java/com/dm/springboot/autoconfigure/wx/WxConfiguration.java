package com.dm.springboot.autoconfigure.wx;

import javax.servlet.Servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

import com.dm.wx.controller.WxController;

public class WxConfiguration {

    @ConditionalOnBean(type = { "me.chanjar.weixin.mp.api.WxMpService" })
    @ConditionalOnClass({ Servlet.class, WxController.class })
    static class WxConfigurationImpl {
        @Bean
        public WxController wxController(@Autowired me.chanjar.weixin.mp.api.WxMpService mpservice) {
            WxController controller = new WxController();
            controller.setWxMpService(mpservice);
            return controller;
        }
    }

}
