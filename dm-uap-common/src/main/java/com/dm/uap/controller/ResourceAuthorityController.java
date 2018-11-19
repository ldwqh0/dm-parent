package com.dm.uap.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
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

import com.dm.uap.converter.AuthorityConverter;
import com.dm.uap.converter.ResourceOperationConverter;
import com.dm.uap.dto.ResourceAuthorityDto;
import com.dm.uap.dto.ResourceOperationDto;
import com.dm.uap.entity.Authority;
import com.dm.uap.entity.Resource;
import com.dm.uap.service.AuthorityService;
import com.dm.uap.service.ResourceService;

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

	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResourceAuthorityDto save(@RequestBody ResourceAuthorityDto resourceAuthority) {
		Authority authority = authorityService.save(resourceAuthority);
		return authorityConverter.toResourceAuthorityDto(authority);
	}

	@GetMapping("{roleId}")
	public ResourceAuthorityDto get(@PathVariable("roleId") Long roleId) {
		Optional<Authority> authority = authorityService.get(roleId);
		List<Resource> resources = resourceService.listAll();
		ResourceAuthorityDto result = authorityConverter.toResourceAuthorityDto(authority);
		mergeResourceAuthority(result, resources);
		return result;
	}

	@DeleteMapping("{roleId}")
	public void deleteByRoleId(@PathVariable("roleId") Long roleId) {
		authorityService.deleteResourceAuthoritiesByRoleId(roleId);
	}

	private ResourceAuthorityDto mergeResourceAuthority(ResourceAuthorityDto source, List<Resource> resources) {
		List<ResourceOperationDto> operations = source.getResourceAuthorities();
		List<ResourceOperationDto> normal = resources.stream()
				.filter(resource -> noneContainResource(resource, operations))
				.map(resourceOperationConverter::toDto).collect(Collectors.toList());
		source.setResourceAuthorities((List<ResourceOperationDto>) CollectionUtils.union(operations, normal));
		return source;
	}

	private boolean noneContainResource(Resource resource, List<ResourceOperationDto> operations) {
		return !IterableUtils.matchesAny(operations,
				operation -> Objects.equals(operation.getResource().getId(), resource.getId()));

	}

}
