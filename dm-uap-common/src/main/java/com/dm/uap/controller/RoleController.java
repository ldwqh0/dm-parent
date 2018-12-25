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

import com.dm.common.dto.TableResult;
import com.dm.common.exception.DataValidateException;
import com.dm.uap.converter.RoleConverter;
import com.dm.uap.dto.RoleDto;
import com.dm.uap.entity.Role;
import com.dm.uap.service.RoleService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.http.HttpStatus.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("roles")
@Slf4j
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
		Optional<Role> role = roleService.get(id);
		return roleConverter.toDto(role);
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
	public TableResult<?> list(@PageableDefault(page = 0, size = 10) Pageable pageable,
			@RequestParam(value = "search", required = false) String key,
			@RequestParam(value = "draw", required = false) Long draw) {
		try {
			Page<Role> result = roleService.search(key, pageable);
			return TableResult.success(draw, result, roleConverter::toDto);
		} catch (Exception e) {
			log.error("查询角色时发生错误", e);
			return TableResult.failure(draw, pageable, e.getMessage());
		}
	}

	@ApiOperation("获取所有启用角色")
	@GetMapping
	public List<RoleDto> listEnabled() {
		List<Role> roles = roleService.listAllEnabled();
		return roleConverter.toSimpleDto(roles);
	}
}
