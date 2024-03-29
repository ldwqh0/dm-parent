package com.dm.uap.security.controller;

import com.dm.common.exception.DataNotExistException;
import com.dm.common.exception.DataValidateException;
import com.dm.uap.dto.UserDto;
import com.dm.uap.dto.request.UpdatePasswordDto;
import com.dm.uap.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

/**
 * 当前用户信息
 */
@RestController
@RequestMapping("/users/current")
public class CurrentUserController {

    private final UserService userService;

    public CurrentUserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 获取当前用户的信息
     *
     * @param userId 用户id, 后台自动获取，不要传入
     * @return 用户信息
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

    /**
     * 修改当前用户的密码
     *
     * @param userId 要修改密码的用户,后台自动获取，前端不需要传入
     * @param data   修改密码后的用户信息
     * @return 修改密码之后的用户信息
     */
    @PatchMapping("password")
    @ResponseStatus(CREATED)
    public UserDto changePassword(
        @AuthenticationPrincipal(expression = "id") Long userId,
        @Valid @RequestBody UpdatePasswordDto data) {
        validRePassword(data.getPassword(), data.getRepassword());
        if (userService.checkPassword(userId, data.getOldPassword())) {
            throw new DataValidateException("原始密码校验错误");
        }
        return userService.resetPassword(userId, data.getPassword());
    }

    private void validRePassword(String password, String rePassword) {
        if (!StringUtils.equals(password, rePassword)) {
            throw new DataValidateException("两次密码输入不一致");
        }
    }
}
