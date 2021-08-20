package com.dm.auth.service.impl;

import com.dm.auth.converter.MenuConverter;
import com.dm.auth.dto.MenuDto;
import com.dm.auth.entity.Menu;
import com.dm.auth.entity.QMenu;
import com.dm.auth.entity.Role;
import com.dm.auth.repository.MenuRepository;
import com.dm.auth.repository.RoleRepository;
import com.dm.auth.service.MenuService;
import com.dm.collections.CollectionUtils;
import com.dm.collections.Lists;
import com.dm.common.exception.DataValidateException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    private final MenuConverter menuConverter;

    private final RoleRepository roleRepository;

    private final QMenu qMenu = QMenu.menu;


    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {"AuthorityMenus"}, allEntries = true)
    public MenuDto save(MenuDto menuDto) {
        preCheck(menuDto);
        final Menu menu = new Menu();
        menuConverter.copyProperties(menu, menuDto);
        // 在保存新菜单时，继承父菜单的权限设置
        menuDto.getParent().map(MenuDto::getId)
            .map(menuRepository::getById)
            .ifPresent(parent -> {
                menu.setParent(parent);
                // 添加权限信息
                List<Role> authorities = roleRepository.findByMenu(parent);
                if (CollectionUtils.isNotEmpty(authorities)) {
                    authorities.stream().map(Role::getMenus).forEach(menus -> menus.add(menu));
                }
            });
        Menu menuResult = menuRepository.save(menu);
        // 如果前端没有设置排序，会自动生成一个排序
        if (Objects.isNull(menu.getOrder())) {
            menuResult.setOrder(menu.getId());
        }
        return menuConverter.toDto(menuResult);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    @CacheEvict(cacheNames = {"AuthorityMenus"}, allEntries = true)
    public List<Menu> save(Collection<Menu> menus) {
        List<Menu> result = menuRepository.saveAll(menus);
        initOrder(result);
        return menuRepository.saveAll(result);
    }

    private void initOrder(List<Menu> menus) {
        if (CollectionUtils.isNotEmpty(menus)) {
            menus.forEach(menu -> {
                initOrder(menu.getChildren());
                menu.setOrder(menu.getId());
            });
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {"AuthorityMenus"}, allEntries = true)
    public MenuDto update(long id, MenuDto menuDto) {
        preCheck(menuDto);
        Menu menu = menuRepository.getById(id);
        menuConverter.copyProperties(menu, menuDto);
        menu.setParent(null);
        menuDto.getParent()
            .map(MenuDto::getId)
            .map(menuRepository::getById)
            .ifPresent(menu::setParent);
        menuRepository.save(menu);
        return menuConverter.toDto(menu);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MenuDto> findById(long id) {
        return menuRepository.findById(id).map(menuConverter::toDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {"AuthorityMenus"}, allEntries = true)
    public void delete(long id) {
        Menu menu = menuRepository.getById(id);
        List<Role> roles = roleRepository.findByMenu(menu);
        // 删除菜单之前，先移除相关的权限配置信息
        if (CollectionUtils.isNotEmpty(roles)) {
            for (Role role : roles) {
                role.getMenus().remove(menu);
            }
        }
        // 保存修改的角色信息
        roleRepository.saveAll(roles);
        // 删除菜单
        menuRepository.delete(menu);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MenuDto> search(Long parentId, Boolean enabled, String key, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        if (Objects.isNull(parentId)) {
            builder.and(qMenu.parent.isNull());
        } else {
            builder.and(qMenu.parent.id.eq(parentId));
        }
        if (!Objects.isNull(enabled)) {
            builder.and(qMenu.enabled.eq(enabled));
        }
        if (StringUtils.isNotBlank(key)) {
            builder.and(qMenu.name.containsIgnoreCase(key)
                .or(qMenu.title.containsIgnoreCase(key))
                .or(qMenu.url.containsIgnoreCase(key))
                .or(qMenu.description.containsIgnoreCase(key)));
        }
        return menuRepository.findAll(builder, pageable)
            .map(menu -> {
                MenuDto dto = menuConverter.toDto(menu);
                dto.setChildrenCount(menuRepository.childrenCount(menu));
                return dto;
            });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {"AuthorityMenus"}, allEntries = true)
    public MenuDto patch(long id, MenuDto source) {
        Menu menu = menuRepository.getById(id);
        if (Objects.nonNull(source.getEnabled())) {
            menu.setEnabled(source.getEnabled());
        }
        return menuConverter.toDto(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {"AuthorityMenus"}, allEntries = true)
    public Menu moveUp(long id) {
        Menu one = menuRepository.getById(id);
        Long order = one.getOrder();
        Menu parent = one.getParent();
        BooleanExpression exp;
        if (Objects.isNull(parent)) {
            exp = qMenu.parent.isNull();
        } else {
            exp = qMenu.parent.eq(parent);
        }
        exp = exp.and(QMenu.menu.order.lt(order));
        Sort sort = Sort.by(Direction.DESC, "order");
        PageRequest request = PageRequest.of(0, 1, sort);
        Iterable<Menu> thats = menuRepository.findAll(exp, request);
        Iterator<Menu> iterator = thats.iterator();
        if (iterator.hasNext()) {
            Menu that = iterator.next();
            one.setOrder(that.getOrder());
            that.setOrder(order);
        }
        return one;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {"AuthorityMenus"}, allEntries = true)
    public Menu moveDown(long id) {
        Menu one = menuRepository.getById(id);
        Long order = one.getOrder();
        BooleanExpression expression;
        Menu parent = one.getParent();
        if (Objects.isNull(parent)) {
            expression = qMenu.parent.isNull();
        } else {
            expression = qMenu.parent.eq(parent);
        }
        expression = expression.and(QMenu.menu.order.gt(order));
        Sort sort = Sort.by(Direction.ASC, "order");
        PageRequest request = PageRequest.of(0, 1, sort);
        Iterator<Menu> thats = menuRepository.findAll(expression, request).iterator();
        if (thats.hasNext()) {
            Menu that = thats.next();
            one.setOrder(that.getOrder());
            that.setOrder(order);
        }
        return one;
    }

    /**
     * 在保存前对菜单进行校验
     */
    private void preCheck(MenuDto menu) {
        Long menuId = menu.getId();
        if (Objects.nonNull(menuId)) {
            Menu parent = menu.getParent().map(MenuDto::getId).flatMap(menuRepository::findById).orElse(null);
            while (Objects.nonNull(parent)) {
                if (Objects.equals(menuId, parent.getId())) {
                    throw new DataValidateException("不能将一个节点的父级节点设置为它自身或它的叶子节点");
                }
                parent = parent.getParent();
            }
        }
    }

    /**
     * 根据菜单类型获取菜单，
     *
     * @param type 要查找的菜单的类型
     * @return 获取到的菜单的列表
     */
    @Cacheable(cacheNames = "AuthorityMenus", key = "'type_'.concat(#p0)", sync = true)
    @Override
    public List<MenuDto> listAllByType(Menu.MenuType type) {
        return Lists.transform(menuRepository.findByType(type), menuConverter::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuDto> listOffspring(Long parentId, Boolean enabled, Sort sort) {
        List<Menu> menus;
        if (Objects.isNull(parentId)) {
            menus = menuRepository.findAll();
        } else {
            menus = new ArrayList<>();
            listChildren(menus, parentId, sort);
        }
        return menus.stream()
            .filter(menu -> {
                // 如果只查询已经启用的菜单，则只返回已经启用的菜单
                if (Boolean.TRUE.equals(enabled)) {
                    return isEnabled(menu);
                } else {
                    return true;
                }
            })
            .map(menuConverter::toDto)
            .collect(Collectors.toList());
    }

    /**
     * 判断一个菜单的父级菜单是否被启用
     *
     * @param menu 要检查的菜单项
     * @return 如果被启用返回true, 否则返回false
     */
    private boolean isEnabled(Menu menu) {
        return menu == null || (menu.isEnabled() && isEnabled(menu.getParent()));
    }

    @Override
    public boolean exists() {
        return menuRepository.count() > 0;
    }

    @Override
    public boolean existsByName(String name, Long exclude) {
        BooleanBuilder query = new BooleanBuilder();
        query.and(qMenu.name.eq(name));
        if (!Objects.isNull(exclude)) {
            query.and(qMenu.id.notIn(exclude));
        }
        return menuRepository.exists(query);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuDto> findParentsByMenuId(Long menuId) {
        if (Objects.isNull(menuId)) {
            return Collections.emptyList();
        } else {
            ArrayList<MenuDto> parents = new ArrayList<>();
            Menu menu = menuRepository.getById(menuId);
            Menu parent = menu.getParent();
            while (!Objects.isNull(parent)) {
                parents.add(menuConverter.toDto(parent));
                parent = parent.getParent();
            }
            return parents;
        }
    }

    /**
     * 递归的获取所有子菜单项目
     *
     * @param container 保存子菜单项目的容器
     * @param parentId  要查找子菜单的菜单id
     */
    private void listChildren(List<Menu> container, Long parentId, Sort sort) {
        List<Menu> children = menuRepository.findByParentId(parentId, sort);
        container.addAll(children);
        children.forEach(child -> listChildren(container, child.getId(), sort));
    }
}
