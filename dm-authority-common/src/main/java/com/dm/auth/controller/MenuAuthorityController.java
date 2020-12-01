package com.dm.auth.controller;

import com.dm.auth.converter.AuthorityConverter;
import com.dm.auth.converter.MenuConverter;
import com.dm.auth.dto.MenuAuthorityDto;
import com.dm.auth.dto.MenuDto;
import com.dm.auth.entity.Authority;
import com.dm.auth.entity.Menu;
import com.dm.auth.service.AuthorityService;
import com.dm.collections.CollectionUtils;
import com.dm.collections.Lists;
import com.dm.common.exception.DataNotExistException;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping({"menuAuthorities", "p/menuAuthorities"})
public class MenuAuthorityController {

    private final AuthorityService authorityService;

    private final MenuConverter menuConverter;

    private final AuthorityConverter authorityConverter;

    public MenuAuthorityController(
        AuthorityService authorityService,
        MenuConverter menuConverter,
        AuthorityConverter authorityConverter) {
        this.authorityService = authorityService;
        this.menuConverter = menuConverter;
        this.authorityConverter = authorityConverter;
    }

    /**
     * 保存一个角色的菜单权限配置
     *
     * @param roleName     角色名称
     * @param authorityDto 角色授权配置
     * @return 菜单授权配置
     */
    @PutMapping("{roleName}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ResponseStatus(code = HttpStatus.CREATED)
    public MenuAuthorityDto save(@PathVariable("roleName") String roleName,
                                 @RequestBody MenuAuthorityDto authorityDto) {
        authorityDto.setRoleName(roleName);
        Authority menuAuthority = authorityService.save(authorityDto);
        return authorityConverter.toMenuAuthorityDto(menuAuthority);
    }

    /**
     * 获取某个角色的可视菜单，<br />
     * <p>
     * 只会列出明确标记为选中的菜单项。
     *
     * @param roleName 角色名称
     * @return 角色的菜单授权
     */
    @GetMapping("{roleName}")
    @Transactional(readOnly = true)
    public MenuAuthorityDto get(@PathVariable("roleName") String roleName) {
        return authorityService.findByRoleName(roleName).map(authorityConverter::toMenuAuthorityDto)
            .orElseThrow(DataNotExistException::new);
    }

    /**
     * 获取当前用户的可用菜单项，当某个子菜单可用时，父菜单也会被列出来
     * <p>
     * Description: 根据登录用户获取菜单
     *
     * @param user 当前用户信息
     * @return 可见菜单项目的列表
     */
    @ApiOperation("获取当前用户的可用菜单项")
    @GetMapping("current")
    public List<MenuDto> systemMenu(@AuthenticationPrincipal UserDetails user) {
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        if (CollectionUtils.isNotEmpty(authorities)) {
            List<Menu> result = authorities.stream().map(GrantedAuthority::getAuthority)
                .map(authorityService::findByAuthority)
                .flatMap(Set::stream).distinct()
                .sorted((o1, o2) -> (int) (o1.getOrder() - o2.getOrder()))
                .collect(Collectors.toList());
            return Lists.transform(result, menuConverter::toDto);
        } else {
            return Collections.emptyList();
        }
    }
}
