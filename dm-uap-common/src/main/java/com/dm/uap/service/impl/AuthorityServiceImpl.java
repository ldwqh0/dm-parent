package com.dm.uap.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dm.uap.converter.ResourceOperationConverter;
import com.dm.uap.dto.MenuAuthorityDto;
import com.dm.uap.dto.MenuDto;
import com.dm.uap.dto.ResourceAuthorityDto;
import com.dm.uap.dto.ResourceOperationDto;
import com.dm.uap.entity.Menu;
import com.dm.uap.entity.ResourceOperation;
import com.dm.uap.entity.Authority;
import com.dm.uap.entity.Role;
import com.dm.uap.entity.User;
import com.dm.uap.repository.AuthorityRepository;
import com.dm.uap.repository.MenuRepository;
import com.dm.uap.repository.RoleRepository;
import com.dm.uap.repository.UserRepository;
import com.dm.uap.service.AuthorityService;

@Service
public class AuthorityServiceImpl implements AuthorityService {

	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthorityRepository authorityRepository;

	@Autowired
	private ResourceOperationConverter resourceOperationConverter;

	@Override
	@Transactional
	public Authority save(MenuAuthorityDto authorityDto) {
		Role role = roleRepository.getOne(authorityDto.getRoleId());
		Long roleId = authorityDto.getRoleId();
		Authority authority = null;
		if (authorityRepository.existsById(roleId)) {
			authority = authorityRepository.getOne(roleId);
		} else {
			authority = new Authority();
			authority.setRole(role);
			authority = authorityRepository.save(authority);
		}
		List<MenuDto> menus = authorityDto.getAuthorityMenus();
		Set<Menu> list = menus.stream().map(m -> menuRepository.getOne(m.getId())).collect(Collectors.toSet());
		authority.setMenus(list);
		return authority;
	}

	@Override
	@Transactional
	public List<Menu> listUserMenusTree(Long id) {
		User user = userRepository.getOne(id);
		// 获取用户的所有角色
		List<Role> roles = user.getRoles();
		// 添加所有菜单项
		Set<Menu> menus = new HashSet<Menu>();
		// 所有菜单的父级菜单
		Set<Menu> parents = new HashSet<Menu>();
		roles.forEach(role -> {
			if (authorityRepository.existsById(role.getId())) {
				Set<Menu> menus_ = authorityRepository.getOne(role.getId()).getMenus();
				menus.addAll(menus_.stream().filter(m -> m.isEnabled()).collect(Collectors.toSet()));
			} //
		});

		// 递归添加所有父级菜单
		for (Menu menu : menus) {
			addParent(menu, parents);
		}
		menus.addAll(parents);

		Iterator<Menu> menusIteraotr = menus.iterator();
		// 移除所有的不可用的项
		while (menusIteraotr.hasNext()) {
			Menu next = menusIteraotr.next();
			if (!isEnabled(next)) {
				menusIteraotr.remove();
			}
		}

		List<Menu> result = new ArrayList<Menu>();
		result.addAll(menus);
		Collections.sort(result, (o1, o2) -> {
			return (int) (o1.getOrder() - o2.getOrder());
		});

		return result;
	}

	/**
	 * 循环的将某个菜单的父级菜单添加到指定列表
	 * 
	 * @param menu
	 * @param collection
	 */
	private void addParent(Menu menu, Set<Menu> collection) {
		Menu parent = menu.getParent();
		if (parent != null) {
			collection.add(menu.getParent());
			addParent(parent, collection);
		}
	}

	/**
	 * 判断某个菜单是否被禁用，判断的标准是依次判断父级菜单是否被禁用，如果某个菜单的父级菜单被禁用，那么该菜单的子菜单也是被禁用的
	 * 
	 * @param menu
	 * @return
	 */
	private boolean isEnabled(Menu menu) {
		if (menu == null) {
			return true;
		} else {
			if (menu.isEnabled()) {
				return isEnabled(menu.getParent());
			} else {
				return false;
			}
		}
	}

	@Override
	public Optional<Authority> get(Long roleId) {
		return authorityRepository.findById(roleId);
	}

	@Override
	public boolean exists() {
		return authorityRepository.count() > 0;
	}

	@Override
	@Transactional
	public Authority save(ResourceAuthorityDto authorityDto) {
		Role role = roleRepository.getOne(authorityDto.getRoleId());
		Long roleId = authorityDto.getRoleId();
		Authority authority = null;
		if (authorityRepository.existsById(roleId)) {
			authority = authorityRepository.getOne(roleId);
		} else {
			authority = new Authority();
			authority.setRole(role);
			authority = authorityRepository.save(authority);
		}
		List<ResourceOperationDto> _resourceOperations = authorityDto.getResourceAuthority();

		// 保存资源权限配置
		Set<ResourceOperation> resourceOperations = _resourceOperations.stream().map(m -> {
			ResourceOperation operation = new ResourceOperation();
			resourceOperationConverter.copyProperties(operation, m);
			return operation;
		}).collect(Collectors.toSet());
		authority.setResourceOperations(resourceOperations);
		return authority;
	}

	@Override
	public void deleteResourceAuthoritiesByRoleId(Long roleId) {
		if (authorityRepository.existsById(roleId)) {
			Authority authority = authorityRepository.getOne(roleId);
			Set<ResourceOperation> operations = authority.getResourceOperations();
			if (CollectionUtils.isNotEmpty(operations)) {
				operations.clear();
			}
		}
	}

}
