package com.dm.auth.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
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
import com.dm.common.exception.DataNotExistException;
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
    @Transactional
    public MenuAuthorityDto save(@PathVariable("rolename") String rolename,
            @RequestBody MenuAuthorityDto authorityDto) {
        authorityDto.setRoleName(rolename);
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
    @Transactional(readOnly = true)
    public MenuAuthorityDto get(@PathVariable("rolename") String rolename) {
        return authorityService.get(rolename).map(authorityConverter::toMenuAuthorityDto)
                .orElseThrow(DataNotExistException::new);
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
    @Transactional(readOnly = true)
    public List<MenuDto> systemMenu(@AuthenticationPrincipal UserDetailsDto userDto) {
        Collection<GrantedAuthority> authorities = userDto.getRoles();
        if (CollectionUtils.isNotEmpty(authorities)) {
            Set<Menu> menus = authorities.stream().map(GrantedAuthority::getAuthority)
                    .map(authorityService::findByAuthority)
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());
            List<Menu> result = new ArrayList<Menu>();
            result.addAll(menus);
            Collections.sort(result, (o1, o2) -> (int) (o1.getOrder() - o2.getOrder()));
            return menuConverter.toDto(result);
        } else {
            return Collections.emptyList();
        }
    }
}
