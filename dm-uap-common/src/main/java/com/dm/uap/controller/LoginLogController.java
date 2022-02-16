package com.dm.uap.controller;

import com.dm.uap.dto.LoginLogDto;
import com.dm.uap.service.LoginLogService;
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
public class LoginLogController {
    private final LoginLogService loginLogService;

    public LoginLogController(LoginLogService loginLogService) {
        this.loginLogService = loginLogService;
    }

    /**
     * 查询登录请求列表
     *
     * @param pageable 查参数
     * @param keyword  要查询的关键字
     * @return 查询结果
     */
    @GetMapping(params = "draw")
    public Page<LoginLogDto> search(
        Pageable pageable, // 分页请求的入参，是page,size
        @RequestParam(value = "keyword", required = false) String keyword) {
        return loginLogService.list(keyword, pageable);
    }
}
