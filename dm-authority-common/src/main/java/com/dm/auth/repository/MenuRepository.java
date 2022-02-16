package com.dm.auth.repository;

import com.dm.auth.entity.Menu;
import com.dm.data.repository.IdentifiableDtoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface MenuRepository
    extends JpaRepository<Menu, Long>, QuerydslPredicateExecutor<Menu>, IdentifiableDtoRepository<Menu, Long> {

    List<Menu> findByType(Menu.MenuType type);

    List<Menu> findByParentId(Long parentId, Sort sort);

    @Query("select count(m) from Menu m where m.parent=:parent")
    long childrenCount(Menu parent);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long exclude);

    @Query("select max(m.id) from Menu m")
    Optional<Long> findMaxId();
}
