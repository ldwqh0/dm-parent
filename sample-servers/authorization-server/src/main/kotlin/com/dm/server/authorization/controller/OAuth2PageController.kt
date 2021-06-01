package com.dm.server.authorization.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.SessionAttributes
import org.springframework.web.servlet.ModelAndView


/**
 * Oauth2 服务器页面
 *
 * @author 李东
 * @Copyright (C) 2019 江苏云智网络科技股份有限公司版权所有
 * @ignore
 */
@SessionAttributes("authorizationRequest")
@Controller
class OAuth2PageController {
    @GetMapping("/oauth2/login.html", "/oauth2/login.html")
    fun login(): String {
        return "oauth/login.html"
    }

    @GetMapping("/oauth2/logout")
    fun logout(): String {
        return "oauth/logout.html"
    }

    /**
     * 在AuthorizationEndpoint有几个@ExceptionHandler
     *
     * @return
     */
    @GetMapping("/oauth2/error")
    fun error(
        @RequestAttribute("error") error: Exception,
        @RequestParam(value = "client_id", required = false) client: String?,
        @RequestParam(value = "redirect_uri", required = false) redirect: String?
    ): ModelAndView {
        val mv = ModelAndView("oauth/error.html")
        //        if (error instanceof RedirectMismatchException) {
//            message = "请求的redirect_uri: [" + redirect + "] 与该应用(" + client + ")配置的redirect_uri不匹配";
//        } else {
        //        }
        mv.addObject("message", error.message ?: "")
        return mv
    }

    /**
     * 首页直接跳转
     */
    @GetMapping("/", "/oauth2/")
    fun index(): String {
        return "redirect:/oauth2/index.html"
    }
}
