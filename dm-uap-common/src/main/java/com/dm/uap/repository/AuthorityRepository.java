package com.dm.uap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.dm.uap.entity.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long>, QuerydslPredicateExecutor<Authority> {

}
