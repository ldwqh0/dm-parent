package com.dm.webflux.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("files")
public class TestController {
    
    @PostMapping
    public Mono<String> test(FileRequest fileRequest) {
        System.out.println(fileRequest.getFile());
        return Mono.just("success");
    }
}
