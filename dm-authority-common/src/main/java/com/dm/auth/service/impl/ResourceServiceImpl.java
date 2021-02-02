package com.dm.auth.service.impl;

import com.dm.auth.converter.ResourceConverter;
import com.dm.auth.dto.ResourceDto;
import com.dm.auth.entity.AuthResource;
import com.dm.auth.entity.QAuthResource;
import com.dm.auth.entity.ResourceOperation;
import com.dm.auth.repository.ResourceRepository;
import com.dm.auth.repository.RoleRepository;
import com.dm.auth.service.ResourceService;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;

    private final ResourceConverter resourceConverter;

    private final RoleRepository roleRepository;

    private final QAuthResource qResource = QAuthResource.authResource;

    public ResourceServiceImpl(ResourceRepository resourceRepository, ResourceConverter resourceConverter, RoleRepository authorityRepository) {
        this.resourceRepository = resourceRepository;
        this.resourceConverter = resourceConverter;
        this.roleRepository = authorityRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthResource save(ResourceDto dto) {
        AuthResource resource = new AuthResource();
        resourceConverter.copyProperties(resource, dto);
        return resourceRepository.save(resource);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void deleteById(long id) {
        roleRepository.findByResourceOperationsResourceId(id).forEach(authority -> {
            Map<AuthResource, ResourceOperation> iterator = authority.getResourceOperations();
            iterator.keySet().stream().filter(resource -> Objects.equals(resource.getId(), id))
                .forEach(iterator::remove);
        });
        resourceRepository.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthResource update(long id, ResourceDto dto) {
        AuthResource resource = resourceRepository.getOne(id);
        resourceConverter.copyProperties(resource, dto);
        return resource;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuthResource> search(String keywords, Pageable pageable) {
        BooleanBuilder query = new BooleanBuilder();
        if (StringUtils.isNotBlank(keywords)) {
            query.or(qResource.name.containsIgnoreCase(keywords))
                .or(qResource.description.containsIgnoreCase(keywords))
                .or(qResource.matcher.containsIgnoreCase(keywords));
        }
        return resourceRepository.findAll(query, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AuthResource> findById(long id) {
        return resourceRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthResource> listAll() {
        return resourceRepository.findAll();
    }

    @Override
    public boolean exist() {
        return resourceRepository.count() > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AuthResource> findByName(String name) {
        return resourceRepository.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthResource> findByIdNotIn(Collection<Long> ids) {
        return resourceRepository.findByIdNotIn(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> listScopes() {
        return resourceRepository.listScopes();
    }

}
