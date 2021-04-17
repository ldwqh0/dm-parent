package com.dm.auth.service;

import com.dm.auth.dto.ResourceDto;
import com.dm.auth.entity.AuthResource;
import com.dm.security.authentication.UriResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ResourceService {

    ResourceDto save(ResourceDto resource);

    void deleteById(long id);

    ResourceDto update(long id, ResourceDto _resource);

    Page<AuthResource> search(String keyword, Pageable pageable);

    Optional<AuthResource> findById(long id);

//    List<AuthResource> listAll();

    List<AuthResource> findByIdNotIn(Collection<Long> ids);

    boolean exist();

    Optional<AuthResource> findByName(String name);

    List<String> listScopes();

    List<AuthResource> findByMatcherAnExcludeById(String matcher, UriResource.MatchType matchType, Long exclude);
}
