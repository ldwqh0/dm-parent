package com.dm.uap.dingtalk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dm.uap.dingtalk.service.DDepartmentService;

@RestController
@RequestMapping("dDepartments")
public class DDepartmentControlller {

    @Value("${dingtalk.corp-id}")
    private String corpid;

    @Autowired
    private DDepartmentService dDepartmentService;

    @PostMapping("sync")
    public void sync() {
        dDepartmentService.syncToUap(corpid);
    }

}
