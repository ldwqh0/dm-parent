package com.dm.uap.dingtalk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dm.uap.dingtalk.service.DUserService;

@RestController
@RequestMapping("dUsers")
public class DUserController {

    @Value("${dingtalk.corp-id}")
    private String corpid;

    @Autowired
    private DUserService dUserService;

    @PostMapping("sync")
    public void sync() {
        dUserService.asyncToUap(corpid);
    }

    @GetMapping
    public void listAll() {

    }

}
