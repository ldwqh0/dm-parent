package com.dm.auth.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dm.auth.converter.ResourceOperationConverter;
import com.dm.auth.dto.MenuAuthorityDto;
import com.dm.auth.dto.MenuDto;
import com.dm.auth.dto.ResourceAuthorityDto;
import com.dm.auth.dto.ResourceOperationDto;
import com.dm.auth.entity.Authority;
import com.dm.auth.entity.Menu;
import com.dm.auth.entity.Resource;
import com.dm.auth.entity.ResourceOperation;
import com.dm.auth.repository.AuthorityRepository;
import com.dm.auth.repository.MenuRepository;
import com.dm.auth.repository.ResourceRepository;
import com.dm.auth.service.AuthorityService;
import com.dm.security.access.RequestAuthorityAttribute;

@Service
public class AuthorityServiceImpl implements AuthorityService {

	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private AuthorityRepository authorityRepository;

	@Autowired
	private ResourceOperationConverter resourceOperationConverter;

	@Autowired
	private ResourceRepository resourceReopsitory;

	@Override
	@Transactional
	public Authority save(MenuAuthorityDto authorityDto) {
		Long roleId = authorityDto.getRoleId();
		String roleName = authorityDto.getRoleName();
		Authority authority = null;
		if (authorityRepository.existsById(roleName)) {
			authority = authorityRepository.getOne(roleName);
		} else {
			authority = new Authority();
			authority.setId(roleId);
			authority.setRoleName(roleName);
			authority = authorityRepository.save(authority);
		}
		List<MenuDto> menus = authorityDto.getAuthorityMenus();
		Set<Menu> list = menus.stream().map(MenuDto::getId).map(menuRepository::getOne).collect(Collectors.toSet());
		authority.setMenus(list);
		return authority;
	}

	@Override
	@Transactional
	public List<Menu> listMenuByAuthorities(List<String> ids) {
		// 添加所有菜单项
		// 所有菜单的父级菜单
		Set<Menu> parents = new HashSet<Menu>();
		Set<Menu> menus = ids.stream().filter(authorityRepository::existsById)
				.map(authorityRepository::getOne)
				.map(Authority::getMenus)
				.flatMap(Set::stream)
				.filter(Menu::isEnabled)
				.collect(Collectors.toSet());

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
		Collections.sort(result, (o1, o2) -> (int) (o1.getOrder() - o2.getOrder()));
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
	public Optional<Authority> get(String rolename) {
		return authorityRepository.findById(rolename);
	}

	@Override
	public boolean exists() {
		return authorityRepository.count() > 0;
	}

	@Override
	@Transactional
	public Authority save(ResourceAuthorityDto authorityDto) {
		Long roleId = authorityDto.getRoleId();
		String roleName = authorityDto.getRoleName();
		Authority authority = null;
		if (authorityRepository.existsById(roleName)) {
			authority = authorityRepository.getOne(roleName);
		} else {
			authority = new Authority();
			authority.setId(roleId);
			authority.setRoleName(roleName);
			authority = authorityRepository.save(authority);
		}
		List<ResourceOperationDto> _resourceOperations = authorityDto.getResourceAuthorities();

		// 保存资源权限配置
		Set<ResourceOperation> resourceOperations = _resourceOperations.stream()
				// 如果某组配置全部为空，代表该组未配置，则删除该组配置
				.filter(this::hasAuhtority)
				.map(m -> {
					ResourceOperation operation = new ResourceOperation();
					resourceOperationConverter.copyProperties(operation, m);
					operation.setResource(resourceReopsitory.getOne(m.getResource().getId()));
					return operation;
				}).collect(Collectors.toSet());
		authority.setResourceOperations(resourceOperations);
		return authority;
	}

	@Override
	public void deleteResourceAuthoritiesByRoleName(String rolename) {
		if (authorityRepository.existsById(rolename)) {
			Authority authority = authorityRepository.getOne(rolename);
			Set<ResourceOperation> operations = authority.getResourceOperations();
			if (CollectionUtils.isNotEmpty(operations)) {
				operations.clear();
			}
		}
	}

	@Override
	public List<RequestAuthorityAttribute> listAllAttributes() {
		List<Authority> authorities = authorityRepository.findAll();
		List<RequestAuthorityAttribute> attributes = new ArrayList<>();
		for (Authority authority : authorities) {
			Set<ResourceOperation> operations = authority.getResourceOperations();
			String roleName = authority.getRoleName();
			for (ResourceOperation operation : operations) {
				Resource resource = operation.getResource();
				if (!Objects.isNull(operation.getSaveable())) {
					attributes.add(new RequestAuthorityAttribute(
							roleName,
							resource.getMatcher(),
							resource.getMatchType(),
							resource.getScope(),
							HttpMethod.POST,
							operation.getSaveable()));
				}
				if (!Objects.isNull(operation.getReadable())) {
					attributes.add(new RequestAuthorityAttribute(
							roleName,
							resource.getMatcher(),
							resource.getMatchType(),
							resource.getScope(),
							HttpMethod.GET,
							operation.getReadable()));
				}
				if (!Objects.isNull(operation.getUpdateable())) {
					attributes.add(new RequestAuthorityAttribute(
							roleName,
							resource.getMatcher(),
							resource.getMatchType(),
							resource.getScope(),
							HttpMethod.PUT,
							operation.getUpdateable()));
				}

				if (!Objects.isNull(operation.getDeleteable())) {
					attributes.add(new RequestAuthorityAttribute(roleName, resource.getMatcher(),
							resource.getMatchType(),
							resource.getScope(), HttpMethod.DELETE, operation.getDeleteable()));
				}
			}
		}
		return attributes;
	}

	/**
	 * 判断一组资源操作是否被配置<br />
	 * 如果所有的配置都为空，则判定为未配置
	 * 
	 * @param op
	 * @return
	 */
	private boolean hasAuhtority(ResourceOperationDto op) {
		return !Objects.isNull(op.getDeleteable()) ||
				!Objects.isNull(op.getReadable()) ||
				!Objects.isNull(op.getSaveable()) ||
				!Objects.isNull(op.getUpdateable());
	}

}
