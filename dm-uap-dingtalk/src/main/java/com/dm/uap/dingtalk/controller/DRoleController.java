package com.dm.uap.dingtalk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dm.dingtalk.api.service.DingTalkService;

@RestController
@RequestMapping("dRoles")
public class DRoleController {

	@Autowired
	private DingTalkService dingTalkService;

	@PostMapping("sync")
	public void sync() {
		dingTalkService.fetchRoleGroups();
	}

}
