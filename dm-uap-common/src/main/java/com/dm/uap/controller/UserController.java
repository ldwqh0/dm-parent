package com.dm.uap.controller;

import com.dm.common.dto.ValidationResult;
import com.dm.common.exception.DataNotExistException;
import com.dm.common.exception.DataValidateException;
import com.dm.uap.dto.DepartmentDto;
import com.dm.uap.dto.UserDto;
import com.dm.uap.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

/**
 * 用户管理
 */
@RestController
@RequestMapping("users")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 根据用户id获取用户信息
     *
     * @param id 用户ID
     * @return 获取到的用户信息
     */
    @ApiOperation("根据ID获取用户")
    @GetMapping("{id}")
    public UserDto findById(@PathVariable("id") Long id) {
        return userService.findById(id).orElseThrow(DataNotExistException::new);
    }

    /**
     * 新增用户
     *
     * @param userDto 用户信息
     * @return 保存后的用户信息
     */
    @ApiOperation("新增保存用户")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('内置分组_ROLE_ADMIN')")
    @ResponseStatus(CREATED)
    public UserDto save(@RequestBody @Validated({UserDto.New.class, DepartmentDto.ReferenceBy.class}) UserDto userDto) {
        return userService.save(userDto);
    }

    /**
     * 删除一个用户
     *
     * @param id 要删除的用户的ID
     */
    @ApiOperation("删除用户")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('内置分组_ROLE_ADMIN')")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable("id") @Min(value = 3, message = "不能删除内置用户") Long id) {
        userService.delete(id);
    }

    /**
     * 强制更新用户密码
     *
     * @param id         要更新密码的用户
     * @param password   新密码
     * @param rePassword 重复新密码
     * @return 更新密码后的用户
     */
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
        if (StringUtils.equals(password, rePassword)) {
            return userService.resetPassword(id, password);
        } else {
            throw new DataValidateException("两次密码输入不一致");
        }
    }


    /**
     * 更新用户信息
     *
     * @param id      要更新的用户的ID
     * @param userDto 用户信息
     * @return 更新后的用户信息
     */
    @ApiOperation("更新用户")
    @PutMapping("{id}")
    @PreAuthorize("hasAnyAuthority('内置分组_ROLE_ADMIN')")
    @ResponseStatus(CREATED)
    public UserDto update(@PathVariable("id") long id,
                          @Validated({UserDto.Update.class, DepartmentDto.ReferenceBy.class}) @RequestBody UserDto userDto) {
        if (id == 2) {
            throw new DataValidateException("不能修改系统内置匿名用户");
        }
        return userService.update(id, userDto);
    }

    /**
     * 更新用户的部分信息
     *
     * @param id   要更新的用户的ID
     * @param user 用户信息
     * @return 更新后的用户信息
     * @apiNote 暂时只支持修改用户的禁用信息, 其他的暂时不做修改
     */
    @ApiOperation("更新用户指定信息，未明确指定的信息不会被修改")
    @PatchMapping("{id}")
    @PreAuthorize("hasAnyAuthority('内置分组_ROLE_ADMIN')")
    public UserDto patchUpdate(@PathVariable("id") @Min(value = 3, message = "不能修改系统内置匿名用户") long id,
                               @Validated({UserDto.Patch.class, DepartmentDto.ReferenceBy.class}) @RequestBody UserDto user) {
        return userService.patch(id, user);
    }

    /**
     * 获取用户的列表
     *
     * @param department 部门id
     * @param role       角色id
     * @param roleGroup  角色组名称
     * @param keyword    关键字
     * @param pageable   分页信息
     * @return 用户信息的分页响应
     */
    @ApiOperation("列表查询用户")
    @GetMapping
    public Page<UserDto> list(
        @RequestParam(value = "department", required = false) Long department,
        @RequestParam(value = "role", required = false) Long role,
        @RequestParam(value = "roleGroup", required = false) String roleGroup,
        @RequestParam(value = "keyword", required = false) String keyword,
        Pageable pageable) {
        return userService.search(department, role, roleGroup, keyword, pageable);
    }


    /**
     * 校验用户名是否被使用
     *
     * @param id       要排除的用户ID
     * @param username 用户名
     * @return 验证结果
     * @apiNote 当在新建一个用户时，只需要校验用户是否被占用即可，但如果是在修改一个用户时，在验证用户名是否被占用时，需要排除自身
     */
    @GetMapping(value = "validation", params = {"username"})
    public ValidationResult usernameValidation(
        @RequestParam(value = "exclude", required = false) Long id,
        @RequestParam("username") String username) {
        if (userService.userExistsByUsername(username, id)) {
            return ValidationResult.failure("用户名已存在");
        } else {
            return ValidationResult.success();
        }
    }

    /**
     * 验证手机号码是否被占用
     *
     * @param exclude 要排除的用户ID
     * @param mobile  要验证的手机号码
     * @return 验证结果
     */
    @GetMapping(value = "validation", params = {"mobile"})
    public ValidationResult mobileValidation(
        @RequestParam(value = "exclude", required = false) Long exclude,
        @RequestParam("mobile") String mobile) {
        if (userService.userExistsByMobile(mobile, exclude)) {
            return ValidationResult.failure("手机号已被注册");
        } else {
            return ValidationResult.success();
        }
    }

    /**
     * 验证用户的email是否被占用
     *
     * @param id    要排除的用户的id
     * @param email 要验证的email
     * @return 验证结果
     */
    @GetMapping(value = "validation", params = {"email"})
    public ValidationResult emailValidation(
        @RequestParam(value = "exclude", required = false) Long id,
        @RequestParam("email") String email) {
        if (userService.userExistsByEmail(email, id)) {
            return ValidationResult.failure("邮箱已被注册");
        } else {
            return ValidationResult.success();
        }
    }
}
