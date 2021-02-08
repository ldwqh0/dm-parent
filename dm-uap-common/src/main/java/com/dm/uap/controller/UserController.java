package com.dm.uap.controller;

import com.dm.common.exception.DataNotExistException;
import com.dm.common.exception.DataValidateException;
import com.dm.security.core.userdetails.UserDetailsDto;
import com.dm.uap.converter.UserConverter;
import com.dm.uap.dto.DepartmentDto;
import com.dm.uap.dto.UpdatePasswordDto;
import com.dm.uap.dto.UserDto;
import com.dm.uap.dto.ValidationResult;
import com.dm.uap.entity.User;
import com.dm.uap.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("users")
@Validated
public class UserController {

    private final UserService userService;

    private final UserConverter userConverter;

    public UserController(UserService userService, UserConverter userConverter) {
        this.userService = userService;
        this.userConverter = userConverter;
    }

    @ApiOperation("根据ID获取用户")
    @GetMapping("{id}")
    public UserDto get(@PathVariable("id") Long id) {
        return userConverter.toDto(userService.get(id).orElseThrow(DataNotExistException::new));
    }

    @ApiOperation("新增保存用户")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('内置分组_ROLE_ADMIN')")
    @ResponseStatus(CREATED)
    public UserDto save(@RequestBody @Validated({UserDto.New.class, DepartmentDto.ReferenceBy.class}) UserDto userDto) {
        return userConverter.toDto(userService.save(userDto));
    }

    @ApiOperation("删除用户")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('内置分组_ROLE_ADMIN')")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable("id") @Min(value = 3, message = "不能删除内置用户") Long id) {
        userService.delete(id);
    }

    @ApiOperation("重置用户密码")
    @PatchMapping(value = {"{id}/password"}, params = {"!oldPassword"})
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
     * @param id          用户ID
     * @param oldPassword 用户旧密码
     * @param password    用户密码
     * @param rePassword  确认密码
     * @return 修改后的用户信息
     */
    @Deprecated
    @ApiOperation("修改用户密码")
    @PatchMapping(value = {"{id}/password"}, params = {"oldPassword"})
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
        if (userService.checkPassword(id, oldPassword)) {
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
        if (userService.checkPassword(id, data.getOldPassword())) {
            throw new DataValidateException("原始密码校验错误");
        }
        return userConverter.toDto(userService.repassword(id, data.getPassword()));
    }

    @ApiOperation("更新用户")
    @PutMapping("{id}")
    @PreAuthorize("hasAnyAuthority('内置分组_ROLE_ADMIN')")
    @ResponseStatus(CREATED)
    public UserDto update(@PathVariable("id") long id,
                          @Validated({UserDto.Update.class, DepartmentDto.ReferenceBy.class}) @RequestBody UserDto userDto) {
        if (id == 2) {
            throw new DataValidateException("不能修改系统内置匿名用户");
        }
        User user = userService.update(id, userDto);
        return userConverter.toDto(user);
    }

    @ApiOperation("更新用户指定信息，未明确指定的信息不会被修改")
    @PatchMapping("{id}")
    @PreAuthorize("hasAnyAuthority('内置分组_ROLE_ADMIN')")
    public UserDto patchUpdate(@PathVariable("id") @Min(value = 3, message = "不能修改系统内置匿名用户") long id,
                               @Validated({UserDto.Patch.class, DepartmentDto.ReferenceBy.class}) @RequestBody UserDto user) {
//        if (id == 2) {
//            throw new DataValidateException("不能修改系统内置匿名用户");
//        }
        return userConverter.toDto(userService.patch(id, user));
    }

    @ApiOperation("列表查询用户")
    @GetMapping
    public Page<UserDto> list(
        @RequestParam(value = "department", required = false) Long department,
        @RequestParam(value = "role", required = false) Long role,
        @RequestParam(value = "roleGroup", required = false) String roleGroup,
        @RequestParam(value = "search", required = false) String key,
        @PageableDefault(sort = {"order"}, direction = Direction.ASC) Pageable pageable) {
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
     * @param id       用户ID
     * @param username 用户名
     * @return 验证结果
     */
    @GetMapping(value = "validation", params = {"username"})
    public ValidationResult usernameValidation(
        @RequestParam(value = "id", required = false) Long id,
        @RequestParam("username") String username) {
        if (userService.userExistsByUsername(id, username)) {
            return ValidationResult.failure("用户名已存在");
        } else {
            return ValidationResult.success();
        }
    }

    @GetMapping(value = "validation", params = {"mobile"})
    public ValidationResult mobileValidation(
        @RequestParam(value = "id", required = false) Long id,
        @RequestParam("mobile") String mobile) {
        if (userService.userExistsByMobile(id, mobile)) {
            return ValidationResult.failure("手机号已被注册");
        } else {
            return ValidationResult.success();
        }
    }

    @GetMapping(value = "validation", params = {"email"})
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
