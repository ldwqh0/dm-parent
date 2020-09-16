package com.dm.uap.controller;

import com.dm.uap.converter.LoginLogConverter;
import com.dm.uap.dto.LoginLogDto;
import com.dm.uap.service.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("loginLogs")
public class LoginLogController {
    private final LoginLogService loginLogService;

    private final LoginLogConverter loginLogConverter;

    @Autowired
    public LoginLogController(LoginLogService loginLogService, LoginLogConverter loginLogConverter) {
        this.loginLogService = loginLogService;
        this.loginLogConverter = loginLogConverter;
    }

    @GetMapping(params = "draw")
    public Page<LoginLogDto> search(
        Pageable pageable, // 分页请求的入参，是page,size
        @RequestParam(value = "key", required = false) String query) {
        return loginLogService.list(query, pageable).map(loginLogConverter::toDto);
    }
}
