package com.dm.security.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dm.security.annotation.CurrentUser;
import com.dm.security.core.userdetails.UserDetailsDto;

@Controller
public class CurrentUserController {
    @GetMapping("/authorities/current")
    @ResponseBody
    public UserDetailsDto currentUser(@CurrentUser UserDetailsDto p) {
        return p;
    }
}
