package com.dm.auth.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.dm.auth.converter.ResourceConverter;
import com.dm.auth.dto.ResourceDto;
import com.dm.auth.entity.QResource;
import com.dm.auth.entity.Resource;
import com.dm.auth.entity.ResourceOperation;
import com.dm.auth.repository.AuthorityRepository;
import com.dm.auth.repository.ResourceRepository;
import com.dm.auth.service.ResourceService;
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
    @Transactional(rollbackFor = Exception.class)
    public Resource save(ResourceDto dto) {
        Resource resource = new Resource();
        resourceConverter.copyProperties(resource, dto);
        return resourceRepository.save(resource);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void deleteById(long id) {
        authorityRepository.findByResourceOperationsResourceId(id).forEach(authority -> {
            Map<Resource, ResourceOperation> iterator = authority.getResourceOperations();
            iterator.keySet().stream().filter(resource -> Objects.equals(resource.getId(), id))
                    .forEach(iterator::remove);
        });
        resourceRepository.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resource update(long id, ResourceDto dto) {
        Resource resource = resourceRepository.getOne(id);
        resourceConverter.copyProperties(resource, dto);
        return resource;
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public Optional<Resource> findById(long id) {
        return resourceRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Resource> listAll() {
        return resourceRepository.findAll();
    }

    @Override
    public boolean exist() {
        return resourceRepository.count() > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Resource> findByName(String name) {
        return resourceRepository.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Resource> findByIdNotIn(Collection<Long> ids) {
        return resourceRepository.findByIdNotIn(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> listScopes() {
        return resourceRepository.listScopes();
    }

}
