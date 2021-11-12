package com.dm.security.web.controller;

import com.dm.security.core.userdetails.UserDetailsDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@RestController
public class CurrentReactiveController {

    @GetMapping({"/authorities/current", "/p/authorities/current"})
    public Mono<UserDetailsDto> currentUser(@AuthenticationPrincipal UserDetailsDto userDetails) {
        return Mono.justOrEmpty(userDetails);
    }

    @RequestMapping("/session")
    public Mono<String> keepalive(WebSession session) {
        return Mono.just(session.getId());
    }
}
