package com.dm.auth.controller;

import com.dm.auth.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"roleGroups", "p/roleGroups"})
@RequiredArgsConstructor
public class RoleGroupController {

    private final RoleService roleService;

    @GetMapping
    public List<String> roleGroups() {
        return roleService.listGroups();
    }
}
