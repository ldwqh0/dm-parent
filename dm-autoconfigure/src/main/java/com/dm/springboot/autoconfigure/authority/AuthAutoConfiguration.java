package com.dm.springboot.autoconfigure.authority;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.dm.auth.dto.MenuAuthorityDto;
import com.dm.auth.dto.MenuDto;
import com.dm.auth.dto.ResourceAuthorityDto;
import com.dm.auth.dto.ResourceDto;
import com.dm.auth.entity.Authority;
import com.dm.auth.entity.Menu;
import com.dm.auth.entity.ResourceOperation;
import com.dm.auth.service.AuthorityService;
import com.dm.auth.service.MenuService;
import com.dm.auth.service.ResourceService;
import com.dm.collections.Maps;
import com.dm.security.authentication.UriResource.MatchType;

@ConditionalOnClass({ Authority.class })
@EntityScan({ "com.dm.auth" })
@EnableJpaRepositories({ "com.dm.auth" })
@ComponentScan({ "com.dm.auth" })
@Import(AuthJCacheConfiguration.class)
public class AuthAutoConfiguration {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private AuthorityService authorityService;

    @PostConstruct
    public void init() {
        // 初始化菜单
        initMenu();

        initResource();

        // 初始化用户菜单授权信息
        initAuthority();
    }

    private void initResource() {
        if (!resourceService.exist()) {
            ResourceDto resource = new ResourceDto();
            resource.setName("default");
            resource.setMatchType(MatchType.ANT_PATH);
            resource.setMatcher("/**");
            resource.setDescription("默认资源类型");
            resourceService.save(resource);
        }
    }

    private void initAuthority() {
        List<Menu> menus = menuService.listAllEnabled(Sort.by("order"));
        // 判断是否
        if (!authorityService.exists()) {
            // 默认角色ID
            Long roleId = 1L;
            MenuAuthorityDto menuAuthority = new MenuAuthorityDto();
            menuAuthority.setRoleId(roleId);
            menuAuthority.setRoleName("内置分组_ROLE_ADMIN");
            Set<MenuDto> menus_ = menus.stream().map(m -> {
                MenuDto md = new MenuDto();
                md.setId(m.getId());
                return md;
            }).collect(Collectors.toSet());

            // 初始化管理员角色的资源权限，默认授予default资源的全部权限
            resourceService.findByName("default").ifPresent(resource -> {
                // 将知道的授权组装为一个授权对象
                ResourceAuthorityDto resourceAuthority = new ResourceAuthorityDto();
                resourceAuthority.setRoleId(roleId);
                resourceAuthority.setRoleName("内置分组_ROLE_ADMIN");
                Map<Long, ResourceOperation> operations = Maps
                        .entry(resource.getId(), ResourceOperation.accessAll())
                        .build();
                resourceAuthority.setResourceAuthorities(operations);
                authorityService.save(resourceAuthority);
            });

            menuAuthority.setAuthorityMenus(menus_);
            authorityService.save(menuAuthority);
        }
    }

    private void initMenu() {
        if (!menuService.exists()) {
            MenuDto menu = new MenuDto();
            menu.setName("home");
            menu.setEnabled(Boolean.TRUE);
            menu.setTitle("首页");
            menu.setUrl("/");
            menuService.save(menu);
        }
    }

}
