package com.dm.uap.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
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

import com.dm.common.dto.TableResultDto;
import com.dm.uap.converter.UserConverter;
import com.dm.uap.dto.UserDto;
import com.dm.uap.entity.User;
import com.dm.uap.service.UserService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("users")
@Slf4j
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserConverter userConverter;

	@ApiOperation("根据ID获取用户")
	@GetMapping("{id}")
	public UserDto get(@PathVariable("id") Long id) {
		Optional<User> user = userService.get(id);
		return userConverter.toDto(user);
	}

	@ApiOperation("新增保存用户")
	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@ResponseStatus(CREATED)
	public UserDto save(@RequestBody UserDto userDto) {
		User user = userService.save(userDto);
		UserDto user_ = userConverter.toDto(user);
		return user_;
	}

	@ApiOperation("删除用户")
	@DeleteMapping("{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@ResponseStatus(NO_CONTENT)
	public void delete(@PathVariable("id") Long id) {
		userService.delete(id);
	}

	@ApiOperation("更新用户")
	@PutMapping("{id}")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@ResponseStatus(CREATED)
	public UserDto update(@PathVariable("id") long id, @RequestBody UserDto userDto) {
		User user = userService.update(id, userDto);
		return userConverter.toDto(user);
	}

	@ApiOperation("列表查询用户")
	@GetMapping
	public TableResultDto<UserDto> list(
			@PageableDefault(page = 0, size = 10, sort = { "order" }, direction = Direction.ASC) Pageable pageable,
			@RequestParam(value = "search", required = false) String key,
			@RequestParam(value = "draw", required = false) Long draw) {
		try {
			Page<User> result = userService.search(key, pageable);
			return TableResultDto.success(draw, result, r -> userConverter.toDto(r));
		} catch (Exception e) {
			log.error("查询用户信息出错", e);
			return TableResultDto.failure(draw, pageable, e.getMessage());
		}
	}
}
