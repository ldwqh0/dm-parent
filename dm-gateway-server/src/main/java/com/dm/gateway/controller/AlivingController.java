package com.dm.gateway.controller;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class AlivingController {

    public final Map<String, String> response;

    public AlivingController() {
        Map<String, String> r = new LinkedHashMap<>();
        r.put("state", "aliving");
        response = Collections.unmodifiableMap(r);
    }

    @GetMapping("status")
    public Mono<Map<String, String>> state() {
        return Mono.<Map<String, String>>just(response);
    }
}
