package com.dm.security.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dm.security.annotation.CurrentUser;
import com.dm.security.core.userdetails.UserDetailsDto;

import reactor.core.publisher.Mono;

@RestController
public class CurrentUserReactiveController {

    @GetMapping("/authorities/current")
    @ResponseBody
    public Mono<UserDetailsDto> currentUser(@CurrentUser UserDetailsDto user) {
        return Mono.just(user);
    }
}
