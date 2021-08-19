package com.dm.auth.repository;

import com.dm.auth.entity.Menu;
import com.dm.common.repository.IdentifiableDtoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface MenuRepository
    extends JpaRepository<Menu, Long>, QuerydslPredicateExecutor<Menu>, IdentifiableDtoRepository<Menu, Long> {

    List<Menu> findByType(Menu.MenuType type);

    List<Menu> findByParentId(Long parentId, Sort sort);


    @Query("select count(m) from Menu m where m.parent=:parent")
    long childrenCount(Menu parent);
}
