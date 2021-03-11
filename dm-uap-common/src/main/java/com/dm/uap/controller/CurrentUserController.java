package com.dm.uap.controller;

import com.dm.common.exception.DataNotExistException;
import com.dm.uap.converter.UserConverter;
import com.dm.uap.dto.UserDto;
import com.dm.uap.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/current")
@RequiredArgsConstructor
public class CurrentUserController {

    private final UserService userService;
    private final UserConverter userConverter;

    /**
     * 获取当前用户的信息
     *
     * @param userId 用户id, 后台自动获取，不要传入
     * @return
     */
    @GetMapping
    public UserDto current(@AuthenticationPrincipal(expression = "id") Long userId) {
        return userService.findById(userId).orElseThrow(DataNotExistException::new);
    }

    /**
     * 更新当前用户信息
     *
     * @param userId 用户id，系统自动获取，不要传入
     * @param user   新的用户信息
     * @return 更新后的用户信息
     */
    @PutMapping
    public UserDto update(@AuthenticationPrincipal(expression = "id") Long userId, @RequestBody UserDto user) {
        return userService.saveOwnerInfo(userId, user);
    }
}
