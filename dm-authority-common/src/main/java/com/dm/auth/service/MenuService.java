package com.dm.auth.service;

import com.dm.auth.dto.MenuDto;
import com.dm.auth.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MenuService {

    Menu save(MenuDto menuDto);

    /**
     * 批量保存菜单信息,用于菜单初始化,其它地方不用
     *
     * @param menus 要保存的菜单列表
     * @return 保存后的菜单列表
     */
    List<Menu> save(Collection<Menu> menus);

    Menu update(long id, MenuDto menuDto);

    Optional<Menu> get(long id);

    void delete(long id);

    Page<Menu> search(Long parentId, String key, Pageable pageable);

    Menu patch(long id, MenuDto _menu);

    Menu moveUp(long id);

    Menu moveDown(long id);

    List<Menu> listAllEnabled(Sort sort);

    boolean exists();

    long count();

}
