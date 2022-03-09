package com.dm.auth.service.impl;

import com.dm.auth.converter.MenuConverter;
import com.dm.auth.dto.MenuDto;
import com.dm.auth.dto.PositionRequest;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    private final RoleRepository roleRepository;

    private final QMenu qMenu = QMenu.menu;

    public MenuServiceImpl(MenuRepository menuRepository, RoleRepository roleRepository) {
        this.menuRepository = menuRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {"AuthorityMenus"}, allEntries = true)
    public MenuDto save(MenuDto menuDto) {
        preCheck(menuDto);
        final Menu menu = copyProperties(new Menu(), menuDto);
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
        return MenuConverter.toDto(menuResult, menuRepository.childrenCount(menuResult));
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
        Menu menu = copyProperties(menuRepository.getById(id), menuDto);
        menu.setParent(null);
        menuDto.getParent()
            .map(MenuDto::getId)
            .map(menuRepository::getById)
            .ifPresent(menu::setParent);
        menuRepository.save(menu);
        return toDto(menu);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MenuDto> findById(long id) {
        return menuRepository.findById(id).map(this::toDto);
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
            .map(this::toDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {"AuthorityMenus"}, allEntries = true)
    public MenuDto patch(long id, MenuDto source) {
        Menu menu = menuRepository.getById(id);
        if (Objects.nonNull(source.getEnabled())) {
            menu.setEnabled(source.getEnabled());
        }
        return toDto(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {"AuthorityMenus"}, allEntries = true)
    public MenuDto move(long id, PositionRequest.Position pos) {
        Menu one = menuRepository.getById(id);
        Long order = one.getOrder();
        BooleanExpression expression = one.getParent()
            .map(qMenu.parent::eq)
            .orElseGet(qMenu.parent::isNull);
        Sort sort;
        if (PositionRequest.Position.DOWN.equals(pos)) {
            expression = expression.and(qMenu.order.gt(order));
            sort = Sort.by(Direction.ASC, "order");
        } else {
            expression = expression.and(qMenu.order.lt(order));
            sort = Sort.by(Direction.DESC, "order");
        }
        Iterator<Menu> thats = menuRepository.findAll(expression, PageRequest.of(0, 1, sort)).iterator();
        if (thats.hasNext()) {
            Menu that = thats.next();
            one.setOrder(that.getOrder());
            that.setOrder(order);
        }
        return this.toDto(one);
    }

    /**
     * 在保存前对菜单进行校验
     */
    private void preCheck(MenuDto menu) {
        Long menuId = menu.getId();
        if (Objects.nonNull(menuId)) {
            Optional<Menu> parent = menu.getParent()
                .map(MenuDto::getId)
                .flatMap(menuRepository::findById);
            while (parent.isPresent()) {
                if (Objects.equals(menuId, parent.get().getId())) {
                    throw new DataValidateException("不能将一个节点的父级节点设置为它自身或它的叶子节点");
                }
                parent = parent.get().getParent();
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
        return Lists.transform(menuRepository.findByType(type), this::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuDto> listOffspring(Long parentId, Boolean enabled, Sort sort) {
        List<Menu> menus;
        if (Objects.isNull(parentId)) {
            menus = menuRepository.findAll(sort);
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
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    /**
     * 判断一个菜单的父级菜单是否被启用
     *
     * @param menu 要检查的菜单项
     * @return 如果被启用返回true, 否则返回false
     */
    private boolean isEnabled(Menu menu) {
        return menu.isEnabled() && menu.getParent().map(this::isEnabled).orElse(true);
    }

    @Override
    public boolean exists() {
        return menuRepository.findMaxId().isPresent();
    }

    @Override
    public boolean existsByName(String name, Long exclude) {
        if (Objects.isNull(exclude)) {
            return menuRepository.existsByName(name);
        } else {
            return menuRepository.existsByNameAndIdNot(name, exclude);
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

    private MenuDto toDto(Menu menu) {
        return MenuConverter.toDto(menu, menuRepository.childrenCount(menu));
    }

    private Menu copyProperties(Menu model, MenuDto dto) {
        model.setName(dto.getName());
        model.setTitle(dto.getTitle());
        model.setEnabled(dto.getEnabled());
        model.setUrl(dto.getUrl());
        model.setIcon(dto.getIcon());
        model.setDescription(dto.getDescription());
        model.setType(dto.getType());
        model.setOrder(dto.getOrder());
        model.setOpenInNewWindow(dto.getOpenInNewWindow());
        return model;
    }
}
