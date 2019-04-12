package com.dm.uap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.dm.uap.entity.RoleGroup;

public interface RoleGroupRepository extends JpaRepository<RoleGroup, Long>, QuerydslPredicateExecutor<RoleGroup> {

}
