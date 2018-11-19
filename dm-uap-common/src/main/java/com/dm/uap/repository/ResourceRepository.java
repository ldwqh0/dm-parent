package com.dm.uap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.dm.uap.entity.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Long>, QuerydslPredicateExecutor<Resource> {

}
