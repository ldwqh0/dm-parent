package com.dm.auth.service;

import com.dm.auth.dto.ResourceDto;
import com.dm.auth.entity.AuthResource;
import com.dm.security.authentication.UriResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ResourceService {

    ResourceDto save(ResourceDto resource);

    void deleteById(long id);

    ResourceDto update(long id, ResourceDto _resource);

    Page<ResourceDto> search(String keyword, Pageable pageable);

    Optional<ResourceDto> findById(long id);

//    List<AuthResource> listAll();

    List<AuthResource> findByIdNotIn(Collection<Long> ids);

    boolean exist();

    Optional<AuthResource> findByName(String name);

    List<String> listScopes();

//    List<AuthResource> findByMatcherAnExcludeById(String matcher, UriResource.MatchType matchType, Long exclude);

    boolean exist(String matcher, UriResource.MatchType matchType, Set<HttpMethod> methods, Long exclude);
}
