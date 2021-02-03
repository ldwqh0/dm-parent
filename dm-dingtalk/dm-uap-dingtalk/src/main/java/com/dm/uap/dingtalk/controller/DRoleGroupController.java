package com.dm.uap.dingtalk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dm.uap.dingtalk.service.DRoleGroupService;

@RestController
@RequestMapping("dRoleGroups")
public class DRoleGroupController {

    @Value("${dingtalk.corp-id}")
    private String corpid;

    @Autowired
    private DRoleGroupService dRoleGroupService;

    @PostMapping("sync")
    public void sync() {
        dRoleGroupService.syncToUap(corpid);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable("id") Long id) {
        dRoleGroupService.deleteById(corpid, id);
    }
}
