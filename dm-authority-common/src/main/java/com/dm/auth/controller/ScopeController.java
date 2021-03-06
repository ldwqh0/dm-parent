package com.dm.auth.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dm.auth.service.ResourceService;

@RequestMapping({ "scopes", "p/scopes" })
@RestController
public class ScopeController {

    private final ResourceService resourceService;

    public ScopeController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping
    public List<String> listScope() {
        return resourceService.listScopes();
    }
}
