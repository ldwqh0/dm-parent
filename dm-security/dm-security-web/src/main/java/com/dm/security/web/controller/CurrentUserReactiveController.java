package com.dm.security.web.controller;

import com.dm.security.core.userdetails.UserDetailsDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class CurrentUserReactiveController {

    @GetMapping({"/authorities/current", "/p/authorities/current"})
    @ResponseBody
    public Mono<UserDetailsDto> currentUser(@AuthenticationPrincipal UserDetailsDto userDetails) {
        return Mono.justOrEmpty(userDetails);
    }
}
