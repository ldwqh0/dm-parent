package com.dm.auth.controller;

import com.dm.auth.converter.RoleConverter;
import com.dm.auth.dto.RoleDto;
import com.dm.auth.entity.Role;
import com.dm.auth.service.RoleService;
import com.dm.collections.Lists;
import com.dm.common.exception.DataNotExistException;
import com.dm.common.exception.DataValidateException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("roles")
@Validated
public class RoleController {

    private final RoleService roleService;

    private final RoleConverter roleConverter;

    @Autowired
    public RoleController(RoleService roleService, RoleConverter roleConverter) {
        this.roleService = roleService;
        this.roleConverter = roleConverter;
    }

    @ApiOperation("保存角色")
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ResponseStatus(CREATED)
    public RoleDto save(@RequestBody @Validated(RoleDto.New.class) RoleDto roleDto) {
        // 不允许将角色添加到内置分组
//        if ("内置分组".equals(roleDto.getGroup().getName())) {
//            throw new DataValidateException("不允许修改内置组定义");
//        }
        if (roleService.nameExist(null, roleDto.getName())) {
            throw new DataValidateException("角色名称被占用");
        } else {
            Role role = roleService.save(roleDto);
            return roleConverter.toDto(role);
        }
    }

    /**
     * 修改角色信息<br>
     * <p>
     * 角色最小ID为4，1、2、3是系统内置角色，禁止修改 <br>
     * <p>
     * 禁止修改
     *
     * @param id      角色ID
     * @param roleDto 角色信息
     * @return 修改后的角色信息
     */
    @ApiOperation("更新角色")
    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ResponseStatus(CREATED)
    public RoleDto update(@PathVariable("id") @Min(4) long id, @RequestBody @Validated({RoleDto.Update.class}) RoleDto roleDto) {
        // 内置分组角色不允许修改
        roleService.get(id)
            .filter(role -> "内置分组".equals(role.getGroup()))
            .ifPresent(role -> {
                throw new DataValidateException("不允许修改内置分组");
            });
        if (roleService.nameExist(id, roleDto.getName())) {
            throw new DataValidateException("角色名称被占用");
        } else {
            Role role = roleService.update(id, roleDto);
            return roleConverter.toDto(role);
        }
    }

    @ApiOperation("获取角色信息")
    @GetMapping("{id}")
    public RoleDto get(@PathVariable("id") long id) {
        return roleConverter.toDto(roleService.get(id).orElseThrow(DataNotExistException::new));
    }

    /**
     * 删除指定的角色，不能删除系统内置的角色<br>
     * 内置的三个角色不允许被修改
     *
     * @param id 待删除的角色的ID
     */
    @ApiOperation("删除角色")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable("id") @Min(4) long id) {
        // 内置分组角色不允许修改
        roleService.delete(id);
    }

    @ApiOperation("查询角色")
    @GetMapping(params = {"page", "size"})
    public Page<RoleDto> list(
        @RequestParam(value = "group", required = false) String group,
        @RequestParam(value = "search", required = false) String key,
        @PageableDefault Pageable pageable) {
        return roleService.search(group, key, pageable).map(roleConverter::toDto);
    }

    @ApiOperation("获取所有启用角色")
    @GetMapping
    public List<RoleDto> listEnabled() {
        return Lists.transform(roleService.listAllEnabled(), roleConverter::toDto);
    }
}
