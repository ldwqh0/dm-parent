package com.dm.uap.dingtalk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dm.dingding.service.DingTalkService;
import com.dm.uap.dingtalk.service.DRoleGroupService;

@RestController
@RequestMapping("dRoleGroups")
public class DRoleGroupController {

	@Autowired
	private DRoleGroupService dRoleGroupService;

	@PostMapping("sync")
	public void sync() {
		dRoleGroupService.syncToUap();
	}

}
