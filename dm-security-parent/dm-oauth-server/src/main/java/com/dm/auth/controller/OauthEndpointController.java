package com.dm.auth.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dm.security.core.userdetails.UserDetailsDto;

@RestController
public class OauthEndpointController {

    @GetMapping({ "/oauth/users/current" })
    public UserDetailsDto getCurrentUser(@AuthenticationPrincipal UserDetailsDto currentUser) {
        return currentUser;
    }
}
