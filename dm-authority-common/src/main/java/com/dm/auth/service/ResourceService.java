package com.dm.auth.service;

import com.dm.auth.dto.ResourceDto;
import com.dm.auth.entity.AuthResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ResourceService {

    AuthResource save(ResourceDto resource);

    void deleteById(long id);

    AuthResource update(long id, ResourceDto _resource);

    Page<AuthResource> search(String keywords, Pageable pageable);

    Optional<AuthResource> findById(long id);

    List<AuthResource> listAll();

    List<AuthResource> findByIdNotIn(Collection<Long> ids);

    boolean exist();

    Optional<AuthResource> findByName(String name);

    List<String> listScopes();

}
