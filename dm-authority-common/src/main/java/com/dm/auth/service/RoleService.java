package com.dm.auth.service;

import com.dm.auth.dto.MenuAuthorityDto;
import com.dm.auth.dto.MenuDto;
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

    /**
     * 判断系统中角色是否存在
     *
     * @return 如果系统中不存在任何角色，返回false，否则返回true
     */
    boolean exist();

    /**
     * 保存角色信息
     *
     * @param dto 角色信息
     * @return 保存后的角色实体
     */
    Role save(RoleDto dto);

    Optional<Role> findById(Long id);

    /**
     * 判断是否存在指定名称的角色是否存在，需要排除掉指定的ID，用于前的段角色重名验证
     *
     * @param id   需要排除的角色id
     * @param name 要验证的角色名称
     * @param group 角色所在的角色组
     * @return 存在返回true, 不存在返回false
     */
    boolean nameExist(Long id,String group, String name);

    Role update(long id, RoleDto roleDto);

    Optional<Role> get(long id);

    void delete(long id);

    Page<Role> search(String group, String key, Pageable pageable);

    /**
     * 列出所有可用角色
     *
     * @return 可用的角色的列表
     */
    List<Role> listAllEnabled();

    /**
     * 根据角色全名查找角色
     *
     * @param authority 角色全民
     * @return 查找结果
     */
    Optional<Role> findByFullname(String authority);

    /**
     * 根据角色全民判断角色是否存在
     *
     * @param authority 角色全名
     * @return 判断结果，如果存在返回true,否则返回false
     */
    boolean existsByFullname(String authority);


    /**
     * 保存角色的菜单授权信息
     *
     * @param roleId       角色id
     * @param authorityDto 角色菜单授权信息
     * @return 更新菜单信息后的角色信息
     */
    Role saveAuthority(Long roleId, MenuAuthorityDto authorityDto);

    /**
     * 保存角色的资源授权信息
     *
     * @param authorityDto 角色的资源授权信息
     * @return 更新授权信息后的角色实体
     */
    Role saveAuthority(ResourceAuthorityDto authorityDto);

    /**
     * 根据角色查询菜单项目
     *
     * @param authority 角色名称
     * @param root 父级菜单，如果指定菜单根，则只返回该根下的菜单可以用菜单
     * @return 角色的授权菜单列表
     */
    Set<MenuDto> findAuthorityMenus(String authority, Long root);

    /**
     * 查询所有的角色组
     *
     * @return 角色组的列表
     */
    List<String> listGroups();
}
