package com.dm.auth.service;

import com.dm.auth.dto.MenuAuthorityDto;
import com.dm.auth.dto.ResourceAuthorityDto;
import com.dm.auth.dto.RoleDto;
import com.dm.auth.entity.Menu;
import com.dm.auth.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    Optional<Role> findByFullname(String authority);

    boolean existsByFullname(String authority);


    /**
     * 保存角色的菜单授权信息
     *
     * @param authorityDto
     * @return
     */
    Role saveAuthority(MenuAuthorityDto authorityDto);

    /**
     * 保存角色的资源授权信息
     *
     * @param authorityDto
     * @return
     */
    Role saveAuthority(ResourceAuthorityDto authorityDto);

    /**
     * 根据角色查询菜单项目
     *
     * @param authority 角色名称
     * @return 角色的授权菜单列表
     */
    Set<Menu> findAuthorityMenus(String authority);

}
