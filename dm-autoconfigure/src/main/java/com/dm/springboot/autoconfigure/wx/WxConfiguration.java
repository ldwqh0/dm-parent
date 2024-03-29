package com.dm.springboot.autoconfigure.wx;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

@ConditionalOnBean(type = {"me.chanjar.weixin.mp.api.WxMpService"})
@ConditionalOnClass(name = {"javax.servlet.Servlet", "com.dm.wx.controller.WxController"})
public class WxConfiguration {

    @Bean
    public com.dm.wx.controller.WxController wxController() {
        return new com.dm.wx.controller.WxController();
    }
}
