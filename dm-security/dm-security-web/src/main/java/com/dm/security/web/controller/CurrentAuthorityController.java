package com.dm.security.web.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

/**
 * 当前用户授权和会话
 */
@Controller
public class CurrentAuthorityController {
    /**
     * 获取当前页用户信息
     *
     * @param principal 用户信息
     * @ignoreParams principal
     */
    @GetMapping({"/authorities/current", "/p/authorities/current"})
    @ResponseBody
    public Object currentUser(@AuthenticationPrincipal Object principal) {
        return principal;
    }

    /**
     * 获取当前会话id     *
     *
     * @param request 请求
     * @return 会话的id
     * @ignoreParams request
     */
    @ResponseBody
    @GetMapping("session")
    public String session(WebRequest request) {
        return request.getSessionId();
    }
}
