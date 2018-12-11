package com.dm.auth.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dm.auth.converter.AuthorityConverter;
import com.dm.auth.converter.ResourceOperationConverter;
import com.dm.auth.dto.ResourceAuthorityDto;
import com.dm.auth.dto.ResourceDto;
import com.dm.auth.dto.ResourceOperationDto;
import com.dm.auth.entity.Authority;
import com.dm.auth.entity.Resource;
import com.dm.auth.service.AuthorityService;
import com.dm.auth.service.ResourceService;

@RestController
@RequestMapping(value = { "resourceAuthorities" })
public class ResourceAuthorityController {

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private AuthorityConverter authorityConverter;

	@Autowired
	private ResourceService resourceService;

	@Autowired
	private ResourceOperationConverter resourceOperationConverter;

	/**
	 * 保存一组资源授权设置
	 * 
	 * @param resourceAuthority
	 * @return
	 */
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResourceAuthorityDto save(@RequestBody ResourceAuthorityDto resourceAuthority) {
		Authority authority = authorityService.save(resourceAuthority);
		return authorityConverter.toResourceAuthorityDto(authority);
	}

	/**
	 * 获取指定角色的资源授权设置
	 * 
	 * @param roleId
	 * @return
	 */
	@GetMapping("{rolename}")
	public ResourceAuthorityDto get(@PathVariable("rolename") String rolename) {
		Optional<Authority> authority = authorityService.get(rolename);
		ResourceAuthorityDto result;

		// 没有被权限设置所包含的资源
		List<Resource> notIncludeResource = Collections.emptyList();
		if (authority.isPresent() && CollectionUtils.isNotEmpty(authority.get().getResourceOperations())) {
			result = authorityConverter.toResourceAuthorityDto(authority);
			// 获取没有被资源权限设置所包含的资源
			List<Long> existResource = result.getResourceAuthorities().stream()
					.map(ResourceOperationDto::getResource)
					.map(ResourceDto::getId)
					.collect(Collectors.toList());
			notIncludeResource = resourceService.findByIdNotIn(existResource);
		} else {
			notIncludeResource = resourceService.listAll();
			result = new ResourceAuthorityDto();
			result.setRoleName(rolename);
		}
		List<ResourceOperationDto> operations = result.getResourceAuthorities();
		if (CollectionUtils.isNotEmpty(notIncludeResource)) {
			List<ResourceOperationDto> defaultOperations = notIncludeResource.stream()
					.map(resourceOperationConverter::toDto)
					.collect(Collectors.toList());
			operations.addAll(defaultOperations);
		}
		return result;
	}

	/**
	 * 删除指定角色的资源授权设置
	 * 
	 * @param roleId
	 */
	@DeleteMapping("{rolename}")
	public void deleteByRoleId(@PathVariable("rolename") String rolename) {
		authorityService.deleteResourceAuthoritiesByRoleName(rolename);
	}

}
