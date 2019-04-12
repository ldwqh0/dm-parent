package com.dm.uap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dm.uap.converter.RoleGroupConverter;
import com.dm.uap.service.RoleGroupService;

@RestController
@RequestMapping("roleGroups")
public class RoleGroupController {

	@Autowired
	private RoleGroupService roleGroupService;

	@Autowired
	private RoleGroupConverter roleGroupConverter;

}
