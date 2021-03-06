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
import com.dm.common.exception.DataValidateException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
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
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    private final MenuConverter menuConverter;

    private final RoleRepository roleRepository;

    private final QMenu qMenu = QMenu.menu;

    public MenuServiceImpl(MenuRepository menuRepository, MenuConverter menuConverter,
                           RoleRepository authorityRepository) {
        this.menuRepository = menuRepository;
        this.menuConverter = menuConverter;
        this.roleRepository = authorityRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {"AuthorityMenus"}, allEntries = true)
    public Menu save(MenuDto menuDto) {
        preCheck(menuDto);
        final Menu menu = new Menu();
        menuConverter.copyProperties(menu, menuDto);
        // 在保存新菜单时，继承父菜单的权限设置
        menuDto.getParent().map(MenuDto::getId)
            .map(menuRepository::getOne)
            .ifPresent(parent -> {
                menu.setParent(parent);
                // 添加权限信息
                List<Role> authorities = roleRepository.findByMenu(parent);
                if (CollectionUtils.isNotEmpty(authorities)) {
                    authorities.stream().map(Role::getMenus).forEach(menus -> menus.add(menu));
                }
            });
        Menu menuResult = menuRepository.save(menu);
        menuResult.setOrder(menu.getId());
        return menuResult;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
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
    public Menu update(long id, MenuDto menuDto) {
        preCheck(menuDto);
        Menu menu = menuRepository.getOne(id);
        menuConverter.copyProperties(menu, menuDto);
        menu.setParent(null);
        menuDto.getParent()
            .map(MenuDto::getId)
            .map(menuRepository::getOne)
            .ifPresent(menu::setParent);
        menuRepository.save(menu);
        return menu;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Menu> get(long id) {
        return menuRepository.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {"AuthorityMenus"}, allEntries = true)
    public void delete(long id) {
        Menu menu = menuRepository.getOne(id);
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
    public Page<Menu> search(Long parentId, String key, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        if (Objects.isNull(parentId)) {
            builder.and(qMenu.parent.isNull());
        } else {
            builder.and(qMenu.parent.id.eq(parentId));
        }
        if (StringUtils.isNotBlank(key)) {
            builder.and(qMenu.name.containsIgnoreCase(key)
                .or(qMenu.title.containsIgnoreCase(key))
                .or(qMenu.url.containsIgnoreCase(key))
                .or(qMenu.description.containsIgnoreCase(key)));
        }
        return menuRepository.findAll(builder, pageable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {"AuthorityMenus"}, allEntries = true)
    public Menu patch(long id, MenuDto _menu) {
        Menu menu = menuRepository.getOne(id);
        if (Objects.nonNull(_menu.getEnabled())) {
            menu.setEnabled(_menu.getEnabled());
        }
        return menu;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {"AuthorityMenus"}, allEntries = true)
    public Menu moveUp(long id) {
        Menu one = menuRepository.getOne(id);
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
        Menu one = menuRepository.getOne(id);
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
        if (Objects.nonNull(menu.getId())) {
            Menu parent = menu.getParent().map(MenuDto::getId).flatMap(menuRepository::findById).orElse(null);
            while (Objects.nonNull(parent)) {
                if (menu.getId().equals(parent.getId())) {
                    throw new DataValidateException("不能将一个节点的父级节点设置为它自身或它的叶子节点");
                }
                parent = parent.getParent();
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Menu> listAllEnabled(Sort sort) {
        List<Menu> menus = menuRepository.findByEnabled(true, sort);
        return menus.stream().filter(menu -> !isDisabled(menu)).collect(Collectors.toList());
    }

    /**
     * 判断一个菜单的父级菜单是否被禁用
     *
     * @param menu 要检查的菜单项
     * @return 如果没禁用返回true, 否则返回false
     */
    private boolean isDisabled(Menu menu) {
        if (menu == null) {
            return false;
        } else {
            return !menu.isEnabled() || isDisabled(menu.getParent());
        }
    }

    @Override
    public boolean exists() {
        return menuRepository.count() > 0;
    }

    public long count() {
        return menuRepository.count();
    }

}
