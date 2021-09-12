package com.dm.springboot.autoconfigure.authority;

import com.dm.auth.dto.MenuAuthorityDto;
import com.dm.auth.dto.MenuDto;
import com.dm.auth.dto.ResourceDto;
import com.dm.auth.dto.RoleDto;
import com.dm.auth.entity.Role;
import com.dm.auth.service.MenuService;
import com.dm.auth.service.ResourceService;
import com.dm.auth.service.RoleService;
import com.dm.collections.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Set;

@ConditionalOnClass({Role.class})
@EntityScan({"com.dm.auth"})
@EnableJpaRepositories({"com.dm.auth"})
@ComponentScan({"com.dm.auth"})
@Import(AuthJCacheConfiguration.class)
@RequiredArgsConstructor
@Slf4j
public class AuthAutoConfiguration {

    private final MenuService menuService;

    private final ResourceService resourceService;

    private final RoleService roleService;

    @PostConstruct
    public void init() {
        // 初始化菜单信息
        Set<MenuDto> menus = initMenus();
        // 初始化角色信息
        Set<RoleDto> roles = initRoles(menus);
        // 初始化资源信息
        Set<ResourceDto> resources = initResources(roles);
    }

    private Set<MenuDto> initMenus() {
        if (!menuService.exists()) {
            MenuDto menuDto = new MenuDto();
            menuDto.setName("index");
            menuDto.setTitle("首页");
            menuDto.setUrl("/");
            return Collections.singleton(menuService.save(menuDto));
        } else {
            return Collections.emptySet();
        }
    }

    private Set<ResourceDto> initResources(Set<RoleDto> roles) {
        if (!resourceService.exist()) {
            return Sets.hashSet(
                initResource("default", "/**", "默认资源类型", roles),
                initResource("用户可见菜单", "/p/menuAuthorities/current**/**", "当前用户可见菜单", roles)
            );
        } else {
            return Collections.emptySet();
        }
    }

    private ResourceDto initResource(String name, String matcher, String description, Set<RoleDto> roles) {
        ResourceDto r2 = new ResourceDto();
        r2.setName(name);
        r2.setMatcher(matcher);
        r2.setDescription(description);
        r2.setAccessAuthorities(roles);
        return resourceService.save(r2);
    }

    private Set<RoleDto> initRoles(Set<MenuDto> menus) {
        MenuAuthorityDto authorities = new MenuAuthorityDto();
        authorities.setAuthorityMenus(menus);
        // 增加默认管理员角色
        // id=1
        return Sets.hashSet(
            initRole("ROLE_ADMIN", "系统内置管理员角色", authorities),
            initRole("ROLE_AUTHENTICATED", "系统内置认证通过角色，所有已经登录的用户均为该角色", authorities),
            initRole("ROLE_ANONYMOUS", "系统内置匿名角色", authorities)
        );
    }

    private RoleDto initRole(String name, String description, MenuAuthorityDto authorities) {
        String group = "内置分组";
        String fullname = group + "_" + name;
        if (!roleService.existsByFullname(fullname)) {
            RoleDto role = new RoleDto();
            role.setName(name);
            role.setGroup(group);
            role.setDescription(description);
            role = roleService.save(role);
            roleService.saveAuthority(role.getId(), authorities);
            return role;
        } else {
            return null;
        }
    }

}
