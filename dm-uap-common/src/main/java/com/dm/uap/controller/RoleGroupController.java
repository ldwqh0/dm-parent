package com.dm.uap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dm.uap.converter.RoleGroupConverter;
import com.dm.uap.dto.RoleGroupDto;
import com.dm.uap.service.RoleGroupService;

@RestController
@RequestMapping("roleGroups")
public class RoleGroupController {

	@Autowired
	private RoleGroupService roleGroupService;

	@Autowired
	private RoleGroupConverter roleGroupConverter;

	@GetMapping
	public List<RoleGroupDto> listAll(@PageableDefault(size = 1000) Pageable pageable) {
		return roleGroupConverter.toDto(roleGroupService.search(null, pageable));
	}
	
	@PostMapping
	public RoleGroupDto save(@RequestBody RoleGroupDto data) {
		return roleGroupConverter.toDto(roleGroupService.save(data));
	}

}
