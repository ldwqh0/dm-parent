package com.dm.test.controller;

import java.io.File;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("files")
public class TestController {

    @PostMapping
    public String upload(FileRequest request) {
//        partFile.transferTo(new File("d:\\aa.png"));
//        System.out.println(partFile);
        System.out.println(request.getFile());
        return "success";
    }

}
