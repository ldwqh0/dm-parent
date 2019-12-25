package com.dm.uap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.dm.uap.converter.RoleConverter;
import com.dm.uap.dto.RoleDto;
import com.dm.uap.entity.Role;
import com.dm.uap.service.RoleService;

import io.swagger.annotations.ApiOperation;

import static org.springframework.http.HttpStatus.*;

import java.util.List;

@RestController
@RequestMapping("roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleConverter roleConverter;

    @ApiOperation("保存角色")
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ResponseStatus(CREATED)
    public RoleDto save(@RequestBody RoleDto roleDto) {
        // 不允许将角色添加到内置分组
        if ("内置分组".equals(roleDto.getGroup().getName())) {
            throw new DataValidateException("不允许修改内置组定义");
        }
        if (roleService.nameExist(null, roleDto.getName())) {
            throw new DataValidateException("角色名称被占用");
        } else {
            Role role = roleService.save(roleDto);
            return roleConverter.toDto(role);
        }
    }

    @ApiOperation("更新角色")
    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ResponseStatus(CREATED)
    public RoleDto update(@PathVariable("id") long id, @RequestBody RoleDto roleDto) {
        // 内置分组角色不允许修改
        roleService.get(id)
                .filter(role -> "内置分组".equals(role.getGroup().getName()))
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

    @ApiOperation("删除角色")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable("id") long id) {
        roleService.delete(id);
    }

    @ApiOperation("查询角色")
    @GetMapping(params = { "draw" })
    public Page<RoleDto> list(
            @RequestParam(value = "groupId", required = false) Long groupId,
            @RequestParam(value = "search", required = false) String key,
            @RequestParam(value = "draw", required = false) Long draw,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return roleService.search(groupId, key, pageable).map(roleConverter::toDto);
    }

    @ApiOperation("获取所有启用角色")
    @GetMapping
    public List<RoleDto> listEnabled() {
        List<Role> roles = roleService.listAllEnabled();
        return roleConverter.toDto(roles);
    }
}
