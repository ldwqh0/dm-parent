package com.dm.auth.repository;

import com.dm.auth.entity.Menu;
import com.dm.common.repository.IdentifiableDtoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface MenuRepository
        extends JpaRepository<Menu, Long>, QuerydslPredicateExecutor<Menu>, IdentifiableDtoRepository<Menu, Long> {

//    List<Menu> findByEnabled(boolean b, Sort sort);

    List<Menu> findByType(Menu.MenuType type);

    /**
     * 根据菜单启用状态和父级菜单获取菜单列表<br>
     * 如果parentId为空，则只获取根的菜单
     *
     * @param enabled
     * @param parentId
     * @param sort
     * @return
     */
    List<Menu> findByEnabledAndParentId(Boolean enabled, Long parentId, Sort sort);

//    List<Menu> findByParentId(Long parentId);

}
