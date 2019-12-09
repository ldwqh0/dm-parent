package com.dm.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.dm.auth.entity.Authority;
import com.dm.auth.entity.Menu;

public interface AuthorityRepository extends JpaRepository<Authority, String>, QuerydslPredicateExecutor<Authority> {

    public List<Authority> findByMenu(Menu menu);

    public List<Authority> findByResourceOperationsResourceId(Long resourceId);

    public List<Authority> findByRoleNameIn(List<String> roleName);
}
