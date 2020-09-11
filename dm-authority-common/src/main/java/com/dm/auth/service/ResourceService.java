package com.dm.auth.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dm.auth.dto.ResourceDto;
import com.dm.auth.entity.Resource;

public interface ResourceService {

    public Resource save(ResourceDto resource);

    public void deleteById(long id);

    public Resource update(long id, ResourceDto _resource);

    public Page<Resource> search(String keywords, Pageable pageable);

    public Optional<Resource> findById(long id);

    public List<Resource> listAll();

    public List<Resource> findByIdNotIn(Collection<Long> ids);

    public boolean exist();

    public Optional<Resource> findByName(String name);

    public List<String> listScopes();

}
