package com.dm.uap.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dm.security.annotation.CurrentUser;
import com.dm.security.core.userdetails.UserDetailsDto;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = { "authorities", "oauth2/authorities" })
public class AuthorityController {
	/**
	 * 获取当前用户信息
	 * 
	 * @param currentUser
	 * @return
	 * @throws CloneNotSupportedException
	 */
	@ApiOperation("获取当前用户信息")
	@GetMapping("currentUser")
	public UserDetailsDto currentUser(@CurrentUser UserDetailsDto currentUser) throws CloneNotSupportedException {
		return currentUser;
	}
}
