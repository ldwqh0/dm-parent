package com.dm.webflux.controller;

import java.io.File;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("files")
public class TestController {
    @GetMapping
    public Mono<String> test() {
        return Mono.just("success");
    }

    @PostMapping
    public Mono<String> upload(FileRequest request) {
//        partFile.transferTo(new File("d:\\aa.png"));
//        System.out.println(partFile);
        System.out.println(request.getFile());
        return Mono.just("success");
    }

}
