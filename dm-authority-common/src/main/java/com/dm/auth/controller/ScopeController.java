package com.dm.auth.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dm.auth.service.ResourceService;

@RequestMapping({"scopes", "p/scopes"})
@RestController
@RequiredArgsConstructor
public class ScopeController {

    private final ResourceService resourceService;

    @GetMapping
    public List<String> listScope() {
        return resourceService.listScopes();
    }
}
