package com.dm.springboot.autoconfigure.authority;

import com.dm.auth.dto.MenuAuthorityDto;
import com.dm.auth.dto.MenuDto;
import com.dm.auth.dto.ResourceDto;
import com.dm.auth.dto.RoleDto;
import com.dm.auth.entity.Role;
import com.dm.auth.entity.Role.Status;
import com.dm.auth.service.MenuService;
import com.dm.auth.service.ResourceService;
import com.dm.auth.service.RoleService;
import com.dm.security.authentication.UriResource.MatchType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ConditionalOnClass({Role.class})
@EntityScan({"com.dm.auth"})
@EnableJpaRepositories({"com.dm.auth"})
@ComponentScan({"com.dm.auth"})
@Import(AuthJCacheConfiguration.class)
@RequiredArgsConstructor
public class AuthAutoConfiguration {

    private final MenuService menuService;

    private final ResourceService resourceService;

    private final RoleService authorityService;

    private final RoleService roleService;

    @PostConstruct
    public void init() {

        // 初始化资源信息
        initResource();

        // 初始化角色信息
        initRole();
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

    private void initAuthority(Role role) {
        List<MenuDto> menus = menuService.listOffspring(null, null, Sort.by("order"));
        Long roleId = role.getId();
        // 判断是否
        // 默认角色ID
        MenuAuthorityDto menuAuthority = new MenuAuthorityDto();
        menuAuthority.setRoleName("内置分组_ROLE_ADMIN");
        menuAuthority.setRoleId(roleId);
        Set<MenuDto> menus_ = menus.stream().map(m -> {
            MenuDto md = new MenuDto();
            md.setId(m.getId());
            return md;
        }).collect(Collectors.toSet());

        // 初始化管理员角色的资源权限，默认授予default资源的全部权限
        resourceService.findByName("default").ifPresent(resource -> {
            // 将知道的授权组装为一个授权对象
            // TODO　这里待处理
//            ResourceAuthorityDto resourceAuthority = new ResourceAuthorityDto();
//            resourceAuthority.setRoleName("内置分组_ROLE_ADMIN");
//            resourceAuthority.setRoleId(roleId);
//            Map<Long, ResourceOperation> operations = Maps
//                .entry(resource.getId(), ResourceOperation.accessAll())
//                .build();
//            resourceAuthority.setResourceAuthorities(operations);
//            roleService.saveAuthority(resourceAuthority);
        });

        menuAuthority.setAuthorityMenus(menus_);
        authorityService.saveAuthority(roleId, menuAuthority);
    }

    private void initRole() {
        // 增加默认管理员角色
        if (!roleService.existsByFullname("内置分组_ROLE_ADMIN")) {
            RoleDto role = new RoleDto();
            role.setName("ROLE_ADMIN");
            role.setGroup("内置分组");
            role.setDescription("系统内置管理员角色");
            role.setState(Status.ENABLED);
            Role admin = roleService.save(role);
            initAuthority(admin);
        }
        // 增加默认普通用户角色
        if (!roleService.existsByFullname("内置分组_ROLE_USER")) {
            RoleDto role = new RoleDto();
            role.setName("ROLE_USER");
            role.setGroup("内置分组");
            role.setState(Status.ENABLED);
            role.setDescription("系统内置普通用户角色");
            roleService.save(role);
        }
        // 增加默认匿名用户角色
        if (!roleService.existsByFullname("内置分组_ROLE_ANONYMOUS")) {
            RoleDto role = new RoleDto();
            role.setName("ROLE_ANONYMOUS");
            role.setGroup("内置分组");
            role.setDescription("系统内置匿名角色");
            role.setState(Status.ENABLED);
            roleService.save(role);
        }
    }
}
