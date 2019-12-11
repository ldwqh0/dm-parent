package com.dm.fileserver.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dm.security.core.userdetails.UserDetailsDto;

@RequestMapping("ts")
@RestController
public class TsController {

    @GetMapping
    public Object a(@AuthenticationPrincipal UserDetailsDto b) {
        return b;
    }
}
