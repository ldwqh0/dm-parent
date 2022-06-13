package com.dm.springboot.autoconfigure.authority;

import com.dm.auth.dto.MenuDto;
import com.dm.auth.dto.ResourceDto;
import com.dm.auth.dto.RoleDto;
import com.dm.auth.entity.Role;
import com.dm.auth.service.MenuService;
import com.dm.auth.service.ResourceService;
import com.dm.auth.service.RoleService;
import com.dm.security.authentication.UriResource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Import;

import java.util.Collections;
import java.util.Set;

import static com.dm.collections.Sets.hashSet;

@ConditionalOnClass({Role.class})
@Import({AuthBeanDefineConfiguration.class, AuthJCacheConfiguration.class})
public class AuthAutoConfiguration implements InitializingBean {

    private final MenuService menuService;

    private final ResourceService resourceService;

    private final RoleService roleService;

    public AuthAutoConfiguration(MenuService menuService, ResourceService resourceService, RoleService roleService) {
        this.menuService = menuService;
        this.resourceService = resourceService;
        this.roleService = roleService;
    }

    private Set<MenuDto> initMenus() {
        if (!menuService.exists()) {
            MenuDto menuDto = MenuDto.builder().name("index")
                .title("首页")
                .enabled(true)
                .url("/")
                .build();
            return Collections.singleton(menuService.save(menuDto));
        } else {
            return Collections.emptySet();
        }
    }

    private Set<ResourceDto> initResources(Set<RoleDto> roles) {
        if (!resourceService.exist()) {
            return hashSet(
                initResource("default", "/**", "默认资源类型", roles),
                initResource("用户可见菜单", "/p/menuAuthorities/current**/**", "当前用户可见菜单", roles),
                initResource("当前用户信息", "/p/authorities/current", "当前用户信息", roles)
            );
        } else {
            return Collections.emptySet();
        }
    }

    private ResourceDto initResource(String name, String matcher, String description, Set<RoleDto> roles) {
        ResourceDto resource = ResourceDto.builder()
            .name(name)
            .matcher(matcher)
            .description(description)
            .matchType(UriResource.MatchType.ANT_PATH)
            .accessAuthorities(roles)
            .build();
        return resourceService.save(resource);
    }

    private Set<RoleDto> initRoles(Set<MenuDto> menus) {
        return hashSet(
            initRole(1L, "ROLE_AUTHENTICATED", "系统内置认证通过角色，所有已经登录的用户均为该角色", menus),
            initRole(2L, "ROLE_ANONYMOUS", "系统内置匿名角色", menus),
            initRole(3L, "ROLE_ADMIN", "系统内置管理员角色", menus)
        );
    }

    private RoleDto initRole(Long roleId, String name, String description, Set<MenuDto> menus) {
        String group = "内置分组";
        String fullName = group + "_" + name;
        return roleService.findByFullName(fullName).orElseGet(() -> {
            RoleDto role = roleService.saveWithId(new RoleDto(
                roleId,
                name,
                group,
                description,
                Role.Status.ENABLED
            ));
            roleService.saveAuthority(role.getId(), menus);
            return role;
        });
    }

    @Override
    public void afterPropertiesSet() {
        // 初始化菜单信息
        Set<MenuDto> menus = initMenus();
        // 初始化角色信息
        Set<RoleDto> roles = initRoles(menus);
        // 初始化资源信息
        initResources(roles);
    }
}
