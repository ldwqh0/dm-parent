package com.dm.uap.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.dm.uap.entity.Authority;
import com.dm.uap.entity.Menu;

public interface AuthorityRepository extends JpaRepository<Authority, Long>, QuerydslPredicateExecutor<Authority> {
	public List<Authority> findByMenus(Set<Menu> menus);

	public List<Authority> findByResourceOperationsResourceId(Long resourceId);
}
