package com.dm.uap.service.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dm.common.exception.DataValidateException;
import com.dm.uap.converter.MenuConverter;
import com.dm.uap.dto.MenuDto;
import com.dm.uap.entity.Authority;
import com.dm.uap.entity.Menu;
import com.dm.uap.entity.QMenu;
import com.dm.uap.repository.AuthorityRepository;
import com.dm.uap.repository.MenuRepository;
import com.dm.uap.service.MenuService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

@Service
public class MenuServcieImpl implements MenuService {

	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private MenuConverter menuConverter;

	@Autowired
	private AuthorityRepository authorityRepository;

	private final QMenu qMenu = QMenu.menu;

	@Override
	@Transactional
	public Menu save(MenuDto menuDto) {
		preCheck(menuDto);
		Menu menu = new Menu();
		menuConverter.copyProperties(menu, menuDto);
		MenuDto parentDto = menuDto.getParent();
		// 在保存新菜单时，继承父菜单的权限设置
		if (!Objects.isNull(parentDto) && !Objects.isNull(parentDto.getId())) {
			Menu parent = menuRepository.getOne(parentDto.getId());
			menu.setParent(parent);
			// 添加权限信息
			List<Authority> authorities = authorityRepository.findByMenus(Collections.singleton(parent));
			if (CollectionUtils.isNotEmpty(authorities)) {
				for (Authority authority : authorities) {
					authority.getMenus().add(menu);
				}
			}
		}
		menu = menuRepository.save(menu);
		menu.setOrder(menu.getId());
		return menu;
	}

	@Override
	@Transactional
	public Menu update(long id, MenuDto menuDto) {
		preCheck(menuDto);
		Menu menu = menuRepository.getOne(id);
		menuConverter.copyProperties(menu, menuDto);
		Long parentId = null;
		if (menuDto.getParent() != null) {
			parentId = menuDto.getParent().getId();
		}
		if (!Objects.isNull(parentId)) {
			menu.setParent(menuRepository.getOne(parentId));
		}
		menuRepository.save(menu);
		return menu;
	}

	@Override
	public Optional<Menu> get(Long id) {
		return menuRepository.findById(id);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		Menu menu = menuRepository.getOne(id);
		List<Authority> authorities = authorityRepository.findByMenus(Collections.singleton(menu));
		// 删除菜单之前，先移除相关的权限配置信息
		if (CollectionUtils.isNotEmpty(authorities)) {
			for (Authority authority : authorities) {
				authority.getMenus().remove(menu);
			}
		}
		// 删除菜单
		menuRepository.delete(menu);
	}

	@Override
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
		return menuRepository.findAll(builder.getValue(), pageable);
	}

	@Override
	@Transactional
	public Menu patch(long id, MenuDto _menu) {
		Menu menu = menuRepository.getOne(id);
		if (!Objects.isNull(_menu.getEnabled())) {
			menu.setEnabled(_menu.getEnabled());
		}
		return menu;
	}

	@Override
	@Transactional
	public Menu moveUp(Long id) {
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
		Sort sort = new Sort(Direction.DESC, "order");
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
	@Transactional
	public Menu moveDown(Long id) {
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
		Sort sort = new Sort(Direction.ASC, "order");
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
	 * 
	 * @return
	 */
	private boolean preCheck(MenuDto menu) {
		if (Objects.isNull(menu.getId()) || Objects.isNull(menu.getParent())) {
			return true;
		} else {
			Menu parent = get(menu.getParent().getId()).get();
			while (!Objects.isNull(parent)) {
				if (menu.getId().equals(parent.getId())) {
					throw new DataValidateException("不能将一个节点的父级节点设置为它自身或它的叶子节点");
				}
				parent = parent.getParent();
			}
			return true;
		}
	}

	@Override
	public List<Menu> listAllEnabled(Sort sort) {
		List<Menu> menus = menuRepository.findByEnabled(true, sort);
		return menus.stream().filter(menu -> !isDisabled(menu)).collect(Collectors.toList());
	}

	/**
	 * 判断一个菜单的父级菜单是否被禁用
	 * 
	 * @param menu
	 * @return
	 */
	private boolean isDisabled(Menu menu) {
		if (menu == null) {
			return false;
		} else {
			return menu.isEnabled() ? isDisabled(menu.getParent()) : true;
		}
	}

	@Override
	public boolean exists() {
		return menuRepository.count() > 0;
	}

}
