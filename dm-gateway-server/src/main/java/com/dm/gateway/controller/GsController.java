package com.dm.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import reactor.core.publisher.Mono;

public class GsController {

    @GetMapping("gg")
    @ResponseBody
    public Mono<String> test() {
        return Mono.<String>just("success");
    }
}
