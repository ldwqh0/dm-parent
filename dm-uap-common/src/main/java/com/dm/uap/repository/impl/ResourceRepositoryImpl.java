package com.dm.uap.repository.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.dm.uap.entity.Resource;

@Repository
public class ResourceRepositoryImpl {
	public Page<Resource> find(String key, Pageable pageable) {
		return null;
	}
}
