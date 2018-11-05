package com.dm.uap.service;

import java.util.List;
import java.util.Optional;

import com.dm.uap.dto.AuthorityDto;
import com.dm.uap.entity.Menu;
import com.dm.uap.entity.Authority;

public interface AuthorityService {

	public Authority save(AuthorityDto authorityDto);

	public List<Menu> listUserMenusTree(Long userId);

	public Optional<Authority> get(Long roleId);

	public boolean exists();

}
