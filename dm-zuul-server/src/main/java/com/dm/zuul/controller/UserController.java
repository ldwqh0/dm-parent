package com.dm.zuul.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dm.security.annotation.CurrentUser;
import com.dm.security.core.userdetails.UserDetailsDto;

/**
 * 用户信息
 * 
 * @author 李东
 *
 */
@RestController
@RequestMapping({ "users", "/p/users" })
public class UserController {

    /**
     * 获取当前用
     * 
     * @param user
     * @return
     */
    @GetMapping("current")
    public UserDetailsDto current(@CurrentUser UserDetailsDto user) {
        return user;
    }
}
