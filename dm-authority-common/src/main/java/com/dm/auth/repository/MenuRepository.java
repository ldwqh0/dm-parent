package com.dm.auth.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.dm.auth.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long>, QuerydslPredicateExecutor<Menu> {

	List<Menu> findByEnabled(boolean b, Sort sort);

}