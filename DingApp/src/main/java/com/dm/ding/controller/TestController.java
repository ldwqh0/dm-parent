package com.dm.ding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dm.dingding.service.DingService;

@RestController
@RequestMapping("test")
public class TestController {
	@Autowired
	private DingService dingService;

	@GetMapping("accessToken")
	public Object getAccessToken() {
		return dingService.getAccessToken();
	}

}
