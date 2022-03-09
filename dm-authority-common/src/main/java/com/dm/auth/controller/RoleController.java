package com.dm.auth.controller;

import com.dm.auth.converter.RoleConverter;
import com.dm.auth.dto.RoleDto;
import com.dm.auth.service.RoleService;
import com.dm.collections.Lists;
import com.dm.common.validation.ValidationResult;
import com.dm.common.exception.DataNotExistException;
import com.dm.common.exception.DataValidateException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

/**
 * 角色管理
 */
@RestController
@RequestMapping({"roles", "p/roles"})
@Validated
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 保存一个角色
     *
     * @param roleDto 要包存的角色的信息
     * @return 保存后的角色
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ResponseStatus(CREATED)
    public RoleDto save(@RequestBody @Validated(RoleDto.New.class) RoleDto roleDto) {
        // 不允许将角色添加到内置分组
        if (roleService.existsByFullName(roleDto.getGroup(), roleDto.getName())) {
            throw new DataValidateException("角色名称被占用");
        } else {
            return roleService.save(roleDto);
        }
    }

    /**
     * 修改角色信息
     *
     * @param id      角色ID
     * @param roleDto 角色信息
     * @return 修改后的角色信息
     * @apiNote 角色最小ID为4，1、2、3是系统内置角色，禁止修改 <br>
     */
    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ResponseStatus(CREATED)
    public RoleDto update(@PathVariable("id") @Min(4) long id, @RequestBody @Validated({RoleDto.Update.class}) RoleDto roleDto) {
        if (roleService.existsByFullName(roleDto.getGroup(), roleDto.getName(), id)) {
            throw new DataValidateException("角色名称被占用");
        } else {
            return RoleConverter.toDto(roleService.update(id, roleDto));
        }
    }

    /**
     * 获取角色信息
     *
     * @param id 要获取的角色的id
     * @return 角色的信息
     */
    @GetMapping("{id}")
    public RoleDto findById(@PathVariable("id") long id) {
        return RoleConverter.toDto(roleService.get(id).orElseThrow(DataNotExistException::new));
    }

    /**
     * 删除指定的角色
     *
     * @param id 待删除的角色的ID
     * @apiNote 内置的三个角色不允许被修改, 也就是id<3 的角色不允许被修改
     */
    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable("id") @Min(4) long id) {
        // 内置分组角色不允许修改
        roleService.delete(id);
    }

    /**
     * 查询角色
     *
     * @param group    角色组
     * @param keyword  角色关键字
     * @param pageable 分页信息
     * @return 查询到的角色信息
     */
    @GetMapping(params = {"page", "size"})
    public Page<RoleDto> list(
        @RequestParam(value = "group", required = false) String group,
        @RequestParam(value = "keyword", required = false) String keyword,
        @PageableDefault Pageable pageable) {
        return roleService.search(group, keyword, pageable).map(RoleConverter::toDto);
    }

    /**
     * 获取所有被启用的角色的列表
     *
     * @return 获取到的角色的列表
     */
    @GetMapping
    public List<RoleDto> listEnabled() {
        return Lists.transform(roleService.listAllEnabled(), RoleConverter::toDto);
    }

    /**
     * 验证角色是否存在
     *
     * @param name    要验证的角色
     * @param group   要验证的角色所在的角色组
     * @param exclude 要排除的id
     * @return 验证结果
     */
    @GetMapping("validation")
    public ValidationResult validate(
        @RequestParam("name") String name,
        @RequestParam("group") String group,
        @RequestParam(value = "exclude", required = false) Long exclude) {
        if (roleService.existsByFullName(group, name, exclude)) {
            return ValidationResult.failure("指定角色已经存在");
        } else {
            return ValidationResult.success();
        }
    }
}
