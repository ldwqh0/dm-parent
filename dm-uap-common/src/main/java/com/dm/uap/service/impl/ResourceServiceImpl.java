package com.dm.uap.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dm.uap.converter.ResourceConverter;
import com.dm.uap.dto.ResourceDto;
import com.dm.uap.entity.Resource;
import com.dm.uap.repository.ResourceRepository;
import com.dm.uap.service.ResourceService;

@Service
public class ResourceServiceImpl implements ResourceService {

	@Autowired
	private ResourceRepository resourceRepository;

	@Autowired
	private ResourceConverter resourceConverter;

	@Override
	@Transactional
	public Resource save(ResourceDto dto) {
		Resource resource = new Resource();
		resourceConverter.copyProperties(resource, dto);
		return resourceRepository.save(resource);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		resourceRepository.deleteById(id);
	}

	@Override
	@Transactional
	public Resource update(Long id, ResourceDto dto) {
		Resource resource = resourceRepository.getOne(id);
		resourceConverter.copyProperties(resource, dto);
		return resource;
	}

	@Override
	public Page<Resource> search(String keywords, Pageable pageable) {
		return resourceRepository.find(keywords, pageable);
	}

	@Override
	public Optional<Resource> findById(Long id) {
		return resourceRepository.findById(id);
	}

}
