package com.dm.uap.controller;

import com.dm.uap.converter.LoginLogConverter;
import com.dm.uap.dto.LoginLogDto;
import com.dm.uap.service.LoginLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录日志
 */
@RestController
@RequestMapping("loginLogs")
@RequiredArgsConstructor
public class LoginLogController {
    private final LoginLogService loginLogService;

    private final LoginLogConverter loginLogConverter;


    /**
     * 查询登录请求列表
     * @param pageable 查参数
     * @param query
     * @return
     */
    @GetMapping(params = "draw")
    public Page<LoginLogDto> search(
        Pageable pageable, // 分页请求的入参，是page,size
        @RequestParam(value = "keyword", required = false) String query) {
        return loginLogService.list(query, pageable).map(loginLogConverter::toDto);
    }
}
