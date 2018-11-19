package com.dm.uap.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dm.uap.dto.ResourceDto;
import com.dm.uap.entity.Resource;

public interface ResourceService {

	public Resource save(ResourceDto resource);

	public void deleteById(Long id);

	public Resource update(Long id, ResourceDto _resource);

	public Page<Resource> search(String keywords, Pageable pageable);

	public Optional<Resource> findById(Long id);

	public List<Resource> listAll();

}
