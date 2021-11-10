package com.dm.auth.service;

import com.dm.auth.dto.MenuAuthorityDto;
import com.dm.auth.dto.MenuDto;
import com.dm.auth.dto.RoleDto;
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
    RoleDto save(RoleDto dto);

    /**
     * 带ID保存角色，这个方法不要在业务中使用，通常用于数据初始化。
     * <p>
     * 如果指定id的角色已经存在，不应该修改任何数据
     *
     * @param roleDto 带ID保存
     */
    RoleDto saveWithId(RoleDto roleDto);

    Optional<Role> findById(Long id);

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
    Optional<RoleDto> findByFullName(String authority);

    /**
     * 根据角色全名判断角色是否存在
     *
     * @param authority 角色全名
     * @return 判断结果，如果存在返回true,否则返回false
     */
    default boolean existsByFullName(String authority) {
        String[] groupName = authority.split("\\_", 2);
        return existsByFullName(groupName[0], groupName[1]);
    }

    default boolean existsByFullName(String group, String name) {
        return existsByFullName(group, name, null);
    }

    /**
     * 判断角色是否存在
     *
     * @param group   角色分组
     * @param name    角色名称
     * @param exclude 要排除的项目
     * @return 存在返回true, 不存在返回false
     */
    boolean existsByFullName(String group, String name, Long exclude);

    /**
     * 保存角色的菜单授权信息
     *
     * @param roleId       角色id
     * @param authorityDto 角色菜单授权信息
     * @return 更新菜单信息后的角色信息
     */
    MenuAuthorityDto saveAuthority(Long roleId, MenuAuthorityDto authorityDto);

    /**
     * 根据角色查询菜单项目
     *
     * @param authority 角色名称
     * @param root      父级菜单，如果指定菜单根，则只返回该根下的菜单可以用菜单
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
