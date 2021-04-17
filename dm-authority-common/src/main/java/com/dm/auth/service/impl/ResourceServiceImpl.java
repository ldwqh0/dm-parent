package com.dm.auth.service.impl;

import com.dm.auth.converter.ResourceConverter;
import com.dm.auth.dto.ResourceDto;
import com.dm.auth.entity.AuthResource;
import com.dm.auth.entity.QAuthResource;
import com.dm.auth.entity.Role;
import com.dm.auth.repository.ResourceRepository;
import com.dm.auth.repository.RoleRepository;
import com.dm.auth.service.ResourceService;
import com.dm.collections.CollectionUtils;
import com.dm.collections.Sets;
import com.dm.security.authentication.ResourceAuthorityAttribute;
import com.dm.security.authentication.ResourceAuthorityService;
import com.dm.security.authentication.UriResource;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService, ResourceAuthorityService {

    private final ResourceRepository resourceRepository;

    private final ResourceConverter resourceConverter;

    private final RoleRepository roleRepository;

    private final QAuthResource qResource = QAuthResource.authResource;


    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {"AuthorityAttributes"}, allEntries = true)
    public ResourceDto save(ResourceDto dto) {
        AuthResource resource = new AuthResource();
        copyProperties(resource, dto);
        return resourceConverter.toDto(resourceRepository.save(resource));
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {"AuthorityAttributes"}, allEntries = true)
    public void deleteById(long id) {
        // 删除资源之前先从角色中删除特定资源相关的权限配置
//        roleRepository.findByResourceOperationsResourceId(id).forEach(authority -> {
//            Map<AuthResource, ResourceOperation> iterator = authority.getResourceOperations();
//            iterator.keySet().stream().filter(resource -> Objects.equals(resource.getId(), id))
//                .forEach(iterator::remove);
//        });
        resourceRepository.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {"AuthorityAttributes"}, allEntries = true)
    public ResourceDto update(long id, ResourceDto dto) {
        AuthResource resource = resourceRepository.getOne(id);
        copyProperties(resource, dto);
//        dto.getAccessAuthorities()
        return resourceConverter.toDto(resource);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuthResource> search(String keyword, Pageable pageable) {
        BooleanBuilder query = new BooleanBuilder();
        if (StringUtils.isNotBlank(keyword)) {
            query.or(qResource.name.containsIgnoreCase(keyword))
                .or(qResource.description.containsIgnoreCase(keyword))
                .or(qResource.matcher.containsIgnoreCase(keyword));
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
    @Cacheable(cacheNames = "AuthorityAttributes", key = "'all_resource'", sync = true)
    public Collection<ResourceAuthorityAttribute> listAll() {
        return resourceRepository.findAll().stream()
            .flatMap(this::toResourceAuthorityAttribute)
            .collect(Collectors.toList());
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

    @Override
    @Transactional(readOnly = true)
    public List<AuthResource> findByMatcherAnExcludeById(String matcher, UriResource.MatchType matchType, Long exclude) {
        if (Objects.isNull(exclude)) {
            return resourceRepository.findByMatcherAndMatchType(matcher, matchType);
        } else {
            return resourceRepository.findByMatcherAndMatchTypeAndIdNotIn(matcher, matchType, Collections.singleton(exclude));
        }
    }


    Stream<ResourceAuthorityAttribute> toResourceAuthorityAttribute(AuthResource resource) {
        Set<HttpMethod> methods = resource.getMethods();
        Stream<ResourceAuthorityAttribute> attributes;
        if (CollectionUtils.isEmpty(methods)) {
            attributes = Stream.of(UriResource.of(resource.getMatcher(), resource.getMatchType(), resource.getScope()))
                .map(ResourceAuthorityAttribute::new);
        } else {
            attributes = resource.getMethods().stream()
                .map(method -> UriResource.of(method, resource.getMatcher(), resource.getMatchType(), resource.getScope()))
                .map(ResourceAuthorityAttribute::new);
        }
        return attributes.peek(attribute -> {
            attribute.setDenyAll(resource.isDenyAll());
            attribute.setAuthenticated(resource.isAuthenticated());
            resource.getAccessAuthorities()
                .stream().map(Role::getFullName)
                .forEach(attribute::addAccessAuthority);
            resource.getDenyAuthorities()
                .stream().map(Role::getFullName)
                .forEach(attribute::addDenyAuthority);
        });
    }

    private AuthResource copyProperties(AuthResource authResource, ResourceDto dto) {
        resourceConverter.copyProperties(authResource, dto);
        authResource.getDenyAuthorities().clear();
        authResource.getAccessAuthorities().clear();
        authResource.setAccessAuthorities(Sets.transform(dto.getAccessAuthorities(), roleRepository::getByDto));
        authResource.setDenyAuthorities(Sets.transform(dto.getDenyAuthorities(), roleRepository::getByDto));
        return authResource;
    }
}
