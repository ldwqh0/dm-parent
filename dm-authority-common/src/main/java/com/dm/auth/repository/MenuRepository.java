package com.dm.auth.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.dm.auth.entity.Menu;
import com.dm.common.repository.IdentifiableDtoRepository;

public interface MenuRepository
        extends JpaRepository<Menu, Long>, QuerydslPredicateExecutor<Menu>, IdentifiableDtoRepository<Menu, Long> {

    List<Menu> findByEnabled(boolean b, Sort sort);

}
