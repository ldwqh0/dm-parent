package com.dm.uap.controller;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dm.common.exception.DataNotExistException;
import com.dm.common.exception.DataValidateException;
import com.dm.security.core.userdetails.UserDetailsDto;
import com.dm.uap.converter.UserConverter;
import com.dm.uap.dto.UpdatePasswordDto;
import com.dm.uap.dto.UserDto;
import com.dm.uap.dto.ValidationResult;
import com.dm.uap.entity.User;
import com.dm.uap.service.UserService;

import io.swagger.annotations.ApiOperation;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserConverter userConverter;

    @ApiOperation("根据ID获取用户")
    @GetMapping("{id}")
    public UserDto get(@PathVariable("id") Long id) {
        return userConverter.toDto(userService.get(id).orElseThrow(DataNotExistException::new));
    }

    @ApiOperation("新增保存用户")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('内置分组_ROLE_ADMIN')")
    @ResponseStatus(CREATED)
    public UserDto save(@RequestBody UserDto userDto) {
        return userConverter.toDto(userService.save(userDto));
    }

    @ApiOperation("删除用户")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('内置分组_ROLE_ADMIN')")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable("id") @Min(3) Long id) {
        userService.delete(id);
    }

    @ApiOperation("重置用户密码")
    @PatchMapping(value = { "{id}/password" }, params = { "!oldPassword" })
    @PreAuthorize("hasAnyAuthority('内置分组_ROLE_ADMIN')")
    @ResponseStatus(CREATED)
    public UserDto resetPassword(
            @PathVariable("id") Long id,
            @RequestParam("password") String password,
            @RequestParam("rePassword") String rePassword) {
        if (id == 2) {
            throw new DataValidateException("不能修改系统内置匿名用户");
        }
        validRePassword(password, rePassword);
        return userConverter.toDto(userService.repassword(id, password));
    }

    /**
     * 这个API已经过时
     * 
     * @param id
     * @param oldPassword
     * @param password
     * @param rePassword
     * @return
     */
    @Deprecated
    @ApiOperation("修改用户密码")
    @PatchMapping(value = { "{id}/password" }, params = { "oldPassword" })
    @ResponseStatus(CREATED)
    public UserDto changePassword(
            @PathVariable("id") Long id,
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("password") String password,
            @RequestParam("rePassword") String rePassword) {
        if (id == 2) {
            throw new DataValidateException("不能修改系统内置匿名用户");
        }
        validRePassword(password, rePassword);
        if (!userService.checkPassword(id, oldPassword)) {
            throw new DataValidateException("原始密码校验错误");
        }
        return userConverter.toDto(userService.repassword(id, password));
    }

    @ApiOperation("修改当前用户密码")
    @PatchMapping("current/password")
    @ResponseStatus(CREATED)
    public UserDto changePassword(
            @AuthenticationPrincipal UserDetailsDto user,
            @Valid @RequestBody UpdatePasswordDto data) {
        Long id = user.getId();
        validRePassword(data.getPassword(), data.getRepassword());
        if (!userService.checkPassword(id, data.getOldPassword())) {
            throw new DataValidateException("原始密码校验错误");
        }
        return userConverter.toDto(userService.repassword(id, data.getPassword()));
    }

    @ApiOperation("更新用户")
    @PutMapping("{id}")
    @PreAuthorize("hasAnyAuthority('内置分组_ROLE_ADMIN')")
    @ResponseStatus(CREATED)
    public UserDto update(@PathVariable("id") long id, @RequestBody UserDto userDto) {
        User user = userService.update(id, userDto);
        if (id == 2) {
            throw new DataValidateException("不能修改系统内置匿名用户");
        }
        return userConverter.toDto(user);
    }

    @ApiOperation("列表查询用户")
    @GetMapping
    public Page<UserDto> list(
            @RequestParam(value = "department", required = false) Long department,
            @RequestParam(value = "role", required = false) Long role,
            @RequestParam(value = "roleGroup", required = false) Long roleGroup,
            @RequestParam(value = "search", required = false) String key,
            @PageableDefault(page = 0, size = 10, sort = { "order" }, direction = Direction.ASC) Pageable pageable) {
        Page<User> result = userService.search(department, role, roleGroup, key, pageable);
        return result.map(userConverter::toDto);
    }

    private void validRePassword(String password, String rePassword) {
        if (!StringUtils.equals(password, rePassword)) {
            throw new DataValidateException("两次密码输入不一致");
        }
    }

    /**
     * 校验用户名是否被使用
     * 
     * @param id
     * @param username
     * @return
     */
    @GetMapping(value = "validation", params = { "username" })
    public ValidationResult usernameValidation(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam("username") String username) {
        if (userService.userExistsByUsername(id, username)) {
            return ValidationResult.failure("用户名已存在");
        } else {
            return ValidationResult.success();
        }
    }

    @GetMapping(value = "validation", params = { "mobile" })
    public ValidationResult mobileValidation(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam("mobile") String mobile) {
        if (userService.userExistsByMobile(id, mobile)) {
            return ValidationResult.failure("手机号已被注册");
        } else {
            return ValidationResult.success();
        }
    }

    @GetMapping(value = "validation", params = { "email" })
    public ValidationResult emailValidation(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam("email") String email) {
        if (userService.userExistsByEmail(id, email)) {
            return ValidationResult.failure("邮箱已被注册");
        } else {
            return ValidationResult.success();
        }
    }
}
