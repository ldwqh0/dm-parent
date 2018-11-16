package com.dm.uap.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dm.uap.converter.AuthorityConverter;
import com.dm.uap.dto.ResourceAuthorityDto;
import com.dm.uap.entity.Authority;
import com.dm.uap.service.AuthorityService;

@RestController
@RequestMapping(value = { "resourceAuthorities" })
public class ResourceAuthorityController {

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private AuthorityConverter authorityConverter;

	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResourceAuthorityDto save(ResourceAuthorityDto resourceAuthority) {
		Authority authority = authorityService.save(resourceAuthority);
		return authorityConverter.toResourceAuthorityDto(authority);
	}

	@GetMapping("{roleId}")
	public ResourceAuthorityDto get(@PathVariable("roleId") Long roleId) {
		Optional<Authority> authority = authorityService.get(roleId);
		return authorityConverter.toResourceAuthorityDto(authority);
	}

	@DeleteMapping("{roleId}")
	public void deleteByRoleId(@PathVariable("roleId") Long roleId) {
		authorityService.deleteResourceAuthoritiesByRoleId(roleId);
	}
}
