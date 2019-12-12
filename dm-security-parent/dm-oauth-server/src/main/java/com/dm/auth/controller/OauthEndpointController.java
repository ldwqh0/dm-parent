package com.dm.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dm.security.annotation.CurrentUser;
import com.dm.security.core.userdetails.UserDetailsDto;

@RestController
public class OauthEndpointController {

    @GetMapping({ "/oauth/users/current" })
    @ResponseBody
    public UserDetailsDto getCurrentUser(@CurrentUser UserDetailsDto currentUser) {
        return currentUser;
    }
}
