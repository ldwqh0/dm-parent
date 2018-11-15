package com.dm.uap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
	private AuthorityConverter authorityConverter ;

	@PostMapping
	public ResourceAuthorityDto save(ResourceAuthorityDto resourceAuthority) {

		Authority authority = authorityService.save(resourceAuthority);
		return authorityConverter.toResourceAuthorityDto(authority);

	}

}
