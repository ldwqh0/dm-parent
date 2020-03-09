package com.dm.uap.dingtalk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dm.uap.dingtalk.service.DUserService;

@RestController
@RequestMapping("dUsers")
public class DUserController {

    @Autowired
    private DUserService dUserService;

    @PostMapping("sync")
    @Async
    public void sync() {
        dUserService.asyncToUap();
    }

    @GetMapping
    public void listAll() {

    }

}
