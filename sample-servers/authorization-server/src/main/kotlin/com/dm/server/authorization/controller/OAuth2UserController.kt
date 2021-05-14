package com.dm.server.authorization.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody


/**
 * oauth2 用户信息
 *
 * @author 李东
 * @Copyright (C) 2019 江苏云智网络科技股份有限公司版权所有
 * @ignore
 */
@Controller
class OAuth2UserController {
    /**
     * 获取用户信息
     *
     *
     * 这个是供oauth2 client使用的endpoint,不是管理接口
     *
     *
     * @param principal
     * @return
     */
    @GetMapping("/oauth2/userinfo")
    @ResponseBody
    fun currentUser(@AuthenticationPrincipal principal: Any): Any {
        return principal
    }
}
