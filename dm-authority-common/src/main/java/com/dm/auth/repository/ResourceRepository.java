package com.dm.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.dm.auth.entity.Resource;


public interface ResourceRepository extends JpaRepository<Resource, Long>, QuerydslPredicateExecutor<Resource> {

	public Optional<Resource> findByName(String name);

	public List<Resource> findByIdNotIn(List<Long> ids);

}
