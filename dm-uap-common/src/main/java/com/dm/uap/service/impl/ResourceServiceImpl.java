package com.dm.uap.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.dm.uap.converter.ResourceConverter;
import com.dm.uap.dto.ResourceDto;
import com.dm.uap.entity.Authority;
import com.dm.uap.entity.QResource;
import com.dm.uap.entity.Resource;
import com.dm.uap.entity.ResourceOperation;
import com.dm.uap.repository.AuthorityRepository;
import com.dm.uap.repository.ResourceRepository;
import com.dm.uap.service.ResourceService;
import com.querydsl.core.types.dsl.BooleanExpression;

@Service
public class ResourceServiceImpl implements ResourceService {

	@Autowired
	private ResourceRepository resourceRepository;

	@Autowired
	private ResourceConverter resourceConverter;

	@Autowired
	private AuthorityRepository authorityRepository;

	private final QResource qResource = QResource.resource;

	@Override
	@Transactional
	public Resource save(ResourceDto dto) {
		Resource resource = new Resource();
		resourceConverter.copyProperties(resource, dto);
		return resourceRepository.save(resource);
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void deleteById(Long id) {
		List<Authority> authorities = authorityRepository.findByResourceOperationsResourceId(id);
		for (Authority authority : authorities) {
			Iterator<ResourceOperation> iterator = authority.getResourceOperations().iterator();
			while (iterator.hasNext()) {
				ResourceOperation operation = iterator.next();
				if (Objects.equals(operation.getResource().getId(), id)) {
					iterator.remove();
				}
			}
		}
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
		if (StringUtils.isNotBlank(keywords)) {
			BooleanExpression expression = qResource.description.containsIgnoreCase(keywords);
			expression.or(qResource.matcher.containsIgnoreCase(keywords));
			return resourceRepository.findAll(expression, pageable);
		} else {
			return resourceRepository.findAll(pageable);
		}
	}

	@Override
	public Optional<Resource> findById(Long id) {
		return resourceRepository.findById(id);
	}

	@Override
	public List<Resource> listAll() {
		return resourceRepository.findAll();
	}

	@Override
	public boolean exist() {
		return resourceRepository.count() > 0;
	}

	@Override
	public Optional<Resource> findByName(String name) {
		return resourceRepository.findByName(name);
	}

	@Override
	public List<Resource> findByIdNotIn(List<Long> ids) {
		return resourceRepository.findByIdNotIn(ids);
	}

}
