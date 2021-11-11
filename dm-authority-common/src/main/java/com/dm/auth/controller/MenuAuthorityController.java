package com.dm.auth.controller;

import com.dm.auth.converter.RoleConverter;
import com.dm.auth.dto.MenuAuthorityDto;
import com.dm.auth.dto.MenuDto;
import com.dm.auth.dto.MenuTreeDto;
import com.dm.auth.service.RoleService;
import com.dm.collections.CollectionUtils;
import com.dm.common.exception.DataNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 菜单权限配置
 */
@RestController
@RequestMapping({"menuAuthorities", "p/menuAuthorities"})
public class MenuAuthorityController {

    private final RoleService roleService;

    public MenuAuthorityController(RoleService roleService) {
        this.roleService = roleService;
    }


    /**
     * 保存一个角色的菜单权限配置
     *
     * @param roleId       角色Id
     * @param authorityDto 角色授权配置
     * @return 菜单授权配置
     */
    @PutMapping("{roleId}")
    @PreAuthorize("hasAnyRole('内置分组_ROLE_ADMIN')")
    @ResponseStatus(code = HttpStatus.CREATED)
    public MenuAuthorityDto save(@PathVariable("roleId") Long roleId,
                                 @RequestBody MenuAuthorityDto authorityDto) {
        return roleService.saveAuthority(roleId, authorityDto);
    }

    /**
     * 获取某个角色的可视菜单，
     *
     * @param roleId 角色名称
     * @return 角色的菜单授权
     * @apiNote 只会列出明确标记为选中的菜单项。
     */
    @GetMapping("{roleId}")
    @Transactional(readOnly = true)
    public MenuAuthorityDto get(@PathVariable("roleId") Long roleId) {
        return roleService.findById(roleId).map(RoleConverter::toMenuAuthorityDto)
            .orElseThrow(DataNotExistException::new);
    }

    /**
     * 获取当前用户的可用菜单项
     *
     * @param user 当前用户信息
     * @return 可见菜单项目的列表
     * @apiNote 根据登录用户获取菜单, 当某个子菜单可用时，父菜单也会被列出来
     */
    @GetMapping("current")
    public List<MenuDto> systemMenu(Authentication user,
                                    @RequestParam(value = "parentId", required = false) Long parentId) {
        if (!Objects.isNull(user)) {
            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
            if (CollectionUtils.isNotEmpty(authorities)) {
                return authorities.stream().map(GrantedAuthority::getAuthority)
                    .map((authority) -> roleService.findAuthorityMenus(authority, parentId))
                    .flatMap(Set::stream)
                    .distinct()
                    .sorted(Comparator.comparing(MenuDto::getOrder))
                    .collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    /**
     * 获取当前用户可用菜单项目，返回树形结构
     *
     * @param user
     * @param parentId
     * @return
     */
    @GetMapping(value = "current", params = {"type=tree"})
    public List<MenuTreeDto> systemMenuTree(Authentication user,
                                            @RequestParam(value = "parentId", required = false) Long parentId) {
        List<MenuTreeDto> menus = this.systemMenu(user, parentId).stream().map(this::toTree).collect(Collectors.toList());
        Map<Long, MenuTreeDto> menuMap = menus.stream().collect(Collectors.toMap(MenuTreeDto::getId, Function.identity()));
        menus.forEach(menu -> {
            Long pId = menu.getParentId();
            // 将菜单添加到它的父菜单的children下
            if (!Objects.isNull(pId)) {
                // 如果存在才添加，不存在则不添加
                MenuTreeDto p = menuMap.get(pId);
                if (!Objects.isNull(p)) {
                    p.addChild(menu);
                }
            }
        });
        if (!Objects.isNull(parentId)) {
            return menus.stream().filter(menu -> Objects.equals(menu.getParentId(), parentId)).collect(Collectors.toList());
        } else {
            return menus.stream().filter(menu -> Objects.isNull(menu.getParentId())).collect(Collectors.toList());
        }
    }


    private MenuTreeDto toTree(MenuDto menu) {
        MenuTreeDto treeDto = new MenuTreeDto();
        treeDto.setId(menu.getId());
        menu.getParent().map(MenuDto::getId).ifPresent(treeDto::setParentId);
        treeDto.setName(menu.getName());
        treeDto.setDescription(menu.getDescription());
        treeDto.setEnabled(menu.getEnabled());
        treeDto.setIcon(menu.getIcon());
        treeDto.setOpenInNewWindow(menu.getOpenInNewWindow());
        treeDto.setOrder(menu.getOrder());
        treeDto.setTitle(menu.getTitle());
        treeDto.setType(menu.getType());
        treeDto.setUrl(menu.getUrl());
        return treeDto;
    }
}
