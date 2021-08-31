package com.dm.auth.controller;

import com.dm.auth.service.RoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 角色组
 */
@RestController
@RequestMapping({"roleGroups", "p/roleGroups"})
public class RoleGroupController {

    private final RoleService roleService;

    public RoleGroupController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 获取所有的角色组
     *
     * @return 角色组的列表
     */
    @GetMapping
    public List<String> roleGroups() {
        return roleService.listGroups();
    }
}
