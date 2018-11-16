package com.dm.uap.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dm.uap.entity.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

	public Page<Resource> find(String keywords, Pageable pageable);

}
