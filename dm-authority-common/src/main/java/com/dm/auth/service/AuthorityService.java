package com.dm.auth.service;

import com.dm.auth.dto.MenuAuthorityDto;
import com.dm.auth.dto.ResourceAuthorityDto;
import com.dm.auth.entity.Authority;
import com.dm.auth.entity.Menu;

import java.util.Optional;
import java.util.Set;

public interface AuthorityService {

    /**
     * 保存菜单授权信息
     *
     * @param authorityDto 菜单授权信息
     * @return 保存后的授权信息
     */
    Authority save(MenuAuthorityDto authorityDto);

    Optional<Authority> findByRoleName(String roleName);

    boolean exists();

    /**
     * 保存资源授权信息
     *
     * @param resourceAuthority 要保存的信息
     * @return 保存之后的资源授权
     */
    Authority save(ResourceAuthorityDto resourceAuthority);

    /**
     * 根据角色，删除资源权限配置
     *
     * @param roleName 角色名称
     */
    void deleteResourceAuthoritiesByRoleName(String roleName);

    Set<Menu> findByAuthority(String auth);

}
