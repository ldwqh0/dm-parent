package com.dm.auth.controller;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dm.auth.converter.AuthorityConverter;
import com.dm.auth.converter.MenuConverter;
import com.dm.auth.dto.MenuAuthorityDto;
import com.dm.auth.dto.MenuDto;
import com.dm.auth.entity.Authority;
import com.dm.auth.entity.Menu;
import com.dm.auth.service.AuthorityService;
import com.dm.security.annotation.CurrentUser;
import com.dm.security.core.userdetails.UserDetailsDto;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping({ "menuAuthorities", "p/menuAuthorities" })
public class MenuAuthorityController {

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private MenuConverter menuConverter;

    @Autowired
    private AuthorityConverter authorityConverter;

    /**
     * 保存一个角色的菜单权限配置
     * 
     * @param authorityDto
     * @return
     */
    @PutMapping("{rolename}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ResponseStatus(code = HttpStatus.CREATED)
    public MenuAuthorityDto save(@PathVariable("rolename") String rolename,
            @RequestBody MenuAuthorityDto authorityDto) {
        Authority menuAuthority = authorityService.save(authorityDto);
        return authorityConverter.toMenuAuthorityDto(menuAuthority);
    }

    /**
     * 获取某个角色的可视菜单，<br />
     * 
     * 只会列出明确标记为选中的菜单项。
     * 
     * @param id 角色ID
     * @return
     */
    @GetMapping("{rolename}")
    public MenuAuthorityDto get(@PathVariable("rolename") String rolename) {
        Optional<Authority> authority = authorityService.get(rolename);
        return authorityConverter.toMenuAuthorityDto(authority);
    }

    /**
     * 获取当前用户的可用菜单项，当某个子菜单可用时，父菜单也会被列出来
     * 
     * Description: 根据登录用户获取菜单
     * 
     * @return
     */
    @ApiOperation("获取当前用户的可用菜单项")
    @GetMapping("current")
    public List<MenuDto> systemMenu(@CurrentUser UserDetailsDto userDto) {
        Collection<GrantedAuthority> authorities = userDto.getRoles();
        if (CollectionUtils.isNotEmpty(authorities)) {
            List<String> authorityNames = authorities.stream().map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            List<Menu> menus = authorityService.listMenuByAuthorities(authorityNames);
            return menuConverter.toDto(menus);
        } else {
            return Collections.emptyList();
        }
    }
}
