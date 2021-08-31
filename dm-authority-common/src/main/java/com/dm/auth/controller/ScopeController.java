package com.dm.auth.controller;

import com.dm.auth.service.ResourceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * scope
 */
@RequestMapping({"scopes", "p/scopes"})
@RestController
public class ScopeController {

    private final ResourceService resourceService;

    public ScopeController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    /**
     * 获取所有的scope
     *
     * @return 获取到的scope的列表
     */
    @GetMapping
    public List<String> listScope() {
        return resourceService.listScopes();
    }
}
