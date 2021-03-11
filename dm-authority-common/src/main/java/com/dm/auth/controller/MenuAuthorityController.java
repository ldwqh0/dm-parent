package com.dm.auth.controller;

import com.dm.auth.converter.MenuConverter;
import com.dm.auth.converter.RoleConverter;
import com.dm.auth.dto.MenuAuthorityDto;
import com.dm.auth.dto.MenuDto;
import com.dm.auth.entity.Menu;
import com.dm.auth.entity.Role;
import com.dm.auth.service.RoleService;
import com.dm.collections.CollectionUtils;
import com.dm.collections.Lists;
import com.dm.common.exception.DataNotExistException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单权限配置
 */
@RestController
@RequestMapping({"menuAuthorities", "p/menuAuthorities"})
@RequiredArgsConstructor
public class MenuAuthorityController {

    private final RoleService roleService;

    private final MenuConverter menuConverter;

    private final RoleConverter roleConverter;

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
        Role menuAuthority = roleService.saveAuthority(roleId, authorityDto);
        return roleConverter.toMenuAuthorityDto(menuAuthority);
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
        return roleService.findById(roleId).map(roleConverter::toMenuAuthorityDto)
                .orElseThrow(DataNotExistException::new);
    }

    /**
     * 获取当前用户的可用菜单项
     *
     * @param user 当前用户信息
     * @return 可见菜单项目的列表
     * @apiNote 根据登录用户获取菜单, 当某个子菜单可用时，父菜单也会被列出来
     */
    @ApiOperation("获取当前用户的可用菜单项")
    @GetMapping("current")
    public List<MenuDto> systemMenu(Authentication user,
                                    @RequestParam(value = "parentId", required = false) Long parentId) {
        if (!Objects.isNull(user)) {
            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
            if (CollectionUtils.isNotEmpty(authorities)) {
                List<Menu> result = authorities.stream().map(GrantedAuthority::getAuthority)
                        .map((authority) -> roleService.findAuthorityMenus(authority, parentId)).flatMap(Set::stream).distinct()
                        .sorted(Comparator.comparing(Menu::getOrder)).collect(Collectors.toList());
                return Lists.transform(result, menuConverter::toDto);
            }
        }
        return Collections.emptyList();
    }
}
