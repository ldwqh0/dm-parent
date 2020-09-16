package com.dm.auth.service;

import java.util.Optional;
import java.util.Set;

import com.dm.auth.dto.MenuAuthorityDto;
import com.dm.auth.dto.ResourceAuthorityDto;
import com.dm.auth.entity.Authority;
import com.dm.auth.entity.Menu;

public interface AuthorityService {

    /**
     * 保存菜单授权信息
     * 
     * @param authorityDto
     * @return
     */
    public Authority save(MenuAuthorityDto authorityDto);

    public Optional<Authority> findByRoleName(String rolename);

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
    public void deleteResourceAuthoritiesByRoleName(String rolenamme);

    public Set<Menu> findByAuthority(String auth);

}
