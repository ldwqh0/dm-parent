package com.dm.auth.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.dm.auth.entity.AuthResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dm.auth.dto.ResourceDto;

public interface ResourceService {

    public AuthResource save(ResourceDto resource);

    public void deleteById(long id);

    public AuthResource update(long id, ResourceDto _resource);

    public Page<AuthResource> search(String keywords, Pageable pageable);

    public Optional<AuthResource> findById(long id);

    public List<AuthResource> listAll();

    public List<AuthResource> findByIdNotIn(Collection<Long> ids);

    public boolean exist();

    public Optional<AuthResource> findByName(String name);

    public List<String> listScopes();

}
