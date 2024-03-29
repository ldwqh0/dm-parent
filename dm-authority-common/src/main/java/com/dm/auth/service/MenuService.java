package com.dm.auth.service;

import com.dm.auth.dto.MenuDto;
import com.dm.auth.dto.PositionRequest;
import com.dm.auth.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MenuService {

    MenuDto save(MenuDto menuDto);

    /**
     * 批量保存菜单信息,用于菜单初始化,其它地方不用
     *
     * @param menus 要保存的菜单列表
     * @return 保存后的菜单列表
     */
    List<Menu> save(Collection<Menu> menus);

    MenuDto update(long id, MenuDto menuDto);

    Optional<MenuDto> findById(long id);

    void delete(long id);

    Page<MenuDto> search(Long parentId, Boolean enabled, String key, Pageable pageable);

    MenuDto patch(long id, MenuDto menu);

    MenuDto move(long id, PositionRequest.Position pos);

    List<MenuDto> listAllByType(Menu.MenuType type);

    /**
     * 获取一个菜单的子孙后代菜单项目
     *
     * @param parentId 要获取子孙后代的菜单项,如果为空，代表获取所有菜单项目
     * @param enabled  是否仅获取被启用的菜单项，为false和null表示获取所有菜单
     * @param sort     排序方式
     * @return 获取到的菜单项目
     */
    List<MenuDto> listOffspring(Long parentId, Boolean enabled, Sort sort);

    boolean exists();

    boolean existsByName(String name, Long exclude);

}
