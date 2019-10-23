package com.dm.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dm.security.annotation.CurrentUser;
import com.dm.security.core.userdetails.UserDetailsDto;

@Controller
public class OauthController {

    @GetMapping({ "/oauth/users/current" })
    @ResponseBody
    public UserDetailsDto getCurrentUser(@CurrentUser UserDetailsDto currentUser) {
        return currentUser;
    }
}
