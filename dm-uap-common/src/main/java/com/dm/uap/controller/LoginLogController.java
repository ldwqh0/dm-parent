package com.dm.uap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dm.uap.converter.LoginLogConverter;
import com.dm.uap.dto.LoginLogDto;
import com.dm.uap.service.LoginLogService;

@RestController
@RequestMapping("loginLogs")
public class LoginLogController {
    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private LoginLogConverter loginLogConverter;

    @GetMapping(params = "draw")
    public Page<LoginLogDto> search(
            Pageable pageable,
            @RequestParam(value = "key", required = false) String query) {
        return loginLogService.list(query, pageable).map(loginLogConverter::toDto) ;
    }
}
