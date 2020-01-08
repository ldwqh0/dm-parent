package com.dm.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dm.security.annotation.CurrentUser;
import com.dm.security.core.userdetails.UserDetailsDto;

public class CurrentUserController {
    @GetMapping("/authorities/current")
    @ResponseBody
    public UserDetailsDto currentUser(@CurrentUser UserDetailsDto p) {
        return p;
    }
}
