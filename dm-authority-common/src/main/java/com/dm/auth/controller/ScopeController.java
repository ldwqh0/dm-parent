package com.dm.auth.controller;

import com.dm.auth.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * scop
 */
@RequestMapping({"scopes", "p/scopes"})
@RestController
@RequiredArgsConstructor
public class ScopeController {

    private final ResourceService resourceService;

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
