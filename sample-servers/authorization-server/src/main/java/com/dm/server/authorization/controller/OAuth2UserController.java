package com.dm.server.authorization.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>oauth2 用户信息</p>
 *
 * @author ldwqh0@outlook.com
 */
@Controller
public class OAuth2UserController {

    @GetMapping("/oauth2/userinfo")
    @ResponseBody
    public Object currentUser(@AuthenticationPrincipal Object principal) {
        return principal;
    }

}
