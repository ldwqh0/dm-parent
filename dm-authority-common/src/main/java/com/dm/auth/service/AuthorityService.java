package com.dm.auth.service;

import java.util.List;
import java.util.Optional;

import com.dm.auth.dto.MenuAuthorityDto;
import com.dm.auth.dto.ResourceAuthorityDto;
import com.dm.auth.entity.Authority;
import com.dm.auth.entity.Menu;
import com.dm.security.provider.RequestAuthoritiesService;

public interface AuthorityService extends RequestAuthoritiesService {

	public Authority save(MenuAuthorityDto authorityDto);

//	public List<Menu> listUserMenusTree(Long userId);

	public Optional<Authority> get(Long roleId);

	public boolean exists();

	/**
	 * 保存资源授权信息
	 * 
	 * @param resourceAuthority
	 * @return
	 */
	public Authority save(ResourceAuthorityDto resourceAuthority);

	/**
	 * 根据角色，删除资源权限配置
	 * 
	 * @param roleId
	 */
	public void deleteResourceAuthoritiesByRoleId(Long roleId);

	public List<Menu> listMenuByAuthorities(List<Long> authoritiesIds);

}
