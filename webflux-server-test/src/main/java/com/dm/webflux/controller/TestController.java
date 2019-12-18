package com.dm.webflux.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping
    public Mono<Object> test(@AuthenticationPrincipal Object p) {
        return Mono.just(p);
    }
}
