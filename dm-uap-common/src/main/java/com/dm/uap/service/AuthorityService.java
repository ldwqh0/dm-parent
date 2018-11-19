package com.dm.uap.service;

import java.util.List;
import java.util.Optional;

import com.dm.security.access.RequestAuthorityAttribute;
import com.dm.uap.dto.MenuAuthorityDto;
import com.dm.uap.dto.ResourceAuthorityDto;
import com.dm.uap.entity.Menu;
import com.dm.uap.entity.Authority;

public interface AuthorityService {

	public Authority save(MenuAuthorityDto authorityDto);

	public List<Menu> listUserMenusTree(Long userId);

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

	public List<RequestAuthorityAttribute> listAllAttributes();

}
