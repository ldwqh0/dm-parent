package com.dm.uap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dm.common.dto.TableResultDto;
import com.dm.uap.converter.ResourceConverter;
import com.dm.uap.dto.ResourceDto;
import com.dm.uap.entity.Resource;
import com.dm.uap.service.ResourceService;

import static org.springframework.http.HttpStatus.*;

import java.util.Optional;

@RestController
@RequestMapping("resources")
public class ResourceController {

	@Autowired
	private ResourceService resourceService;

	@Autowired
	private ResourceConverter resourceConverter;

	@PostMapping
	@ResponseStatus(value = CREATED)
	public ResourceDto save(@RequestBody ResourceDto resource) {
		Resource resource_ = resourceService.save(resource);
		return resourceConverter.toDto(resource_);
	}

	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") Long id) {
		resourceService.deleteById(id);
	}

	@PutMapping("{id}")
	@ResponseStatus(value = CREATED)
	public ResourceDto update(@PathVariable("id") Long id, @RequestBody ResourceDto _resource) {
		Resource resource_ = resourceService.update(id, _resource);
		return resourceConverter.toDto(resource_);
	}

	@GetMapping("{id}")
	public ResourceDto get(@PathVariable("id") Long id) {
		Optional<Resource> resource_ = resourceService.findById(id);
		return resourceConverter.toDto(resource_);
	}

	@GetMapping(params = { "draw" })
	public TableResultDto<ResourceDto> search(@RequestParam("draw") Long draw,
			@PageableDefault Pageable pageable,
			@RequestParam(value = "search", required = false) String keywords) {
		Page<Resource> resources = resourceService.search(keywords, pageable);
		return TableResultDto.success(draw, resources, r -> resourceConverter.toDto(r));
	}
}
