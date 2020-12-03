package com.dm.auth.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dm.auth.dto.MenuAuthorityDto;
import com.dm.auth.dto.ResourceAuthorityDto;
import com.dm.auth.dto.RoleDto;
import com.dm.auth.entity.Menu;
import com.dm.auth.entity.Role;

public interface RoleService {

    boolean exist();

    Role save(RoleDto dto);

    Optional<Role> findById(Long id);

    boolean nameExist(Long id, String name);

    Role update(long id, RoleDto roleDto);

    Optional<Role> get(long id);

    void delete(long id);

    Page<Role> search(String group, String key, Pageable pageable);

    /**
     * 列出所有可用角色
     * 
     * @return
     */
    List<Role> listAllEnabled();

    /**
     * 根据角色全面查找角色
     * 
     * @param authority
     * @return
     */
    Optional<Role> findByFullName(String authority);

    Role saveAuthority(MenuAuthorityDto authorityDto);

    /**
     * 
     * @param authorityDto
     * @return
     */
    Role saveAuthority(ResourceAuthorityDto authorityDto);

    /**
     * 根据角色查询菜单项目
     *
     * @param auth 角色名称
     * @return 角色的授权菜单列表
     */
    Set<Menu> findAuthorityMenus(String authority);

    void deleteResourceAuthoritiesByRoleName(String authority);

}
