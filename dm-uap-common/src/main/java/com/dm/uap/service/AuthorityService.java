package com.dm.uap.service;

import java.util.List;
import java.util.Optional;

import com.dm.uap.dto.MenuAuthorityDto;
import com.dm.uap.dto.ResourceAuthorityDto;
import com.dm.uap.entity.Menu;
import com.dm.uap.entity.Authority;

public interface AuthorityService {

	public Authority save(MenuAuthorityDto authorityDto);

	public List<Menu> listUserMenusTree(Long userId);

	public Optional<Authority> get(Long roleId);

	public boolean exists();

	public Authority save(ResourceAuthorityDto resourceAuthority);

}
