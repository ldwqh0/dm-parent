package com.dm.auth.service.impl;

import com.dm.auth.dto.MenuAuthorityDto;
import com.dm.auth.dto.MenuDto;
import com.dm.auth.dto.ResourceAuthorityDto;
import com.dm.auth.entity.AuthResource;
import com.dm.auth.entity.Authority;
import com.dm.auth.entity.Menu;
import com.dm.auth.entity.ResourceOperation;
import com.dm.auth.repository.AuthorityRepository;
import com.dm.auth.repository.MenuRepository;
import com.dm.auth.repository.ResourceRepository;
import com.dm.auth.service.AuthorityService;
import com.dm.collections.Maps;
import com.dm.security.authentication.ResourceAuthorityAttribute;
import com.dm.security.authentication.ResourceAuthorityService;
import com.dm.security.authentication.UriResource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service("authorityService")
public class AuthorityServiceImpl implements AuthorityService, ResourceAuthorityService {

    private final MenuRepository menuRepository;

    private final AuthorityRepository authorityRepository;

    private final ResourceRepository resourceReopsitory;

    // 进行请求限定的请求类型
    private final HttpMethod[] methods = {
        HttpMethod.GET,
        HttpMethod.POST,
        HttpMethod.PUT,
        HttpMethod.DELETE,
        HttpMethod.PATCH
    };

    public AuthorityServiceImpl(MenuRepository menuRepository, AuthorityRepository authorityRepository, ResourceRepository resourceReopsitory) {
        this.menuRepository = menuRepository;
        this.authorityRepository = authorityRepository;
        this.resourceReopsitory = resourceReopsitory;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = "AuthorityMenus", key = "#p0.roleName")
    public Authority save(MenuAuthorityDto authorityDto) {
        Long roleId = authorityDto.getRoleId();
        String roleName = authorityDto.getRoleName();
        Authority authority;
        if (authorityRepository.existsById(roleName)) {
            authority = authorityRepository.getOne(roleName);
        } else {
            authority = new Authority();
            authority.setId(roleId);
            authority.setRoleName(roleName);
            authority = authorityRepository.save(authority);
        }
        Set<MenuDto> menus = authorityDto.getAuthorityMenus();
        Set<Menu> list = menus.stream().map(MenuDto::getId).map(menuRepository::getOne).collect(Collectors.toSet());
        authority.setMenus(list);
        return authority;
    }

    /**
     * 根据角色查询菜单项目
     *
     * @param auth 角色名称
     * @return 角色的授权菜单列表
     */
    // TODO 不能缓存实体
    @Override
    @Cacheable(cacheNames = "AuthorityMenus")
    @Transactional(readOnly = true)
    public Set<Menu> findByAuthority(String auth) {
        Set<Menu> parents = new HashSet<>();
        Set<Menu> menus = authorityRepository.findById(auth)
            .map(Authority::getMenus)
            .orElseGet(Collections::emptySet);
        // 递归添加所有父级菜单
        for (Menu menu : menus) {
            addParent(menu, parents);
        }
        menus.addAll(parents);
        menus.removeIf(next -> !isEnabled(next));
        return menus;
    }

    /**
     * 循环的将某个菜单的父级菜单添加到指定列表
     *
     * @param menu       被添加子菜单父菜单项目
     * @param collection 子菜单列表
     */
    private void addParent(Menu menu, Set<Menu> collection) {
        Menu parent = menu.getParent();
        if (parent != null) {
            collection.add(menu.getParent());
            addParent(parent, collection);
        }
    }

    /**
     * 判断某个菜单是否被禁用，判断的标准是依次判断父级菜单是否被禁用，如果某个菜单的父级菜单被禁用，那么该菜单的子菜单也是被禁用的
     *
     * @param menu 要判断的菜单项
     * @return 返回是否会被禁用
     */
    private boolean isEnabled(Menu menu) {
        if (menu == null) {
            return true;
        } else {
            if (menu.isEnabled()) {
                return isEnabled(menu.getParent());
            } else {
                return false;
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Authority> findByRoleName(String rolename) {
        return authorityRepository.findById(rolename);
    }

    @Override
    public boolean exists() {
        return authorityRepository.count() > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {"AuthorityAttributes"}, key = "'all_resource'")
    public Authority save(ResourceAuthorityDto authorityDto) {
        String roleName = authorityDto.getRoleName();
        Authority authority;
        if (authorityRepository.existsById(roleName)) {
            authority = authorityRepository.getOne(roleName);
        } else {
            authority = new Authority();
        }
        authority.setId(authorityDto.getRoleId());
        authority.setRoleName(roleName);
        authority = authorityRepository.save(authority);
        Map<AuthResource, ResourceOperation> resultOperations = Maps.transformKeys(
            authorityDto.getResourceAuthorities(),
            resourceReopsitory::getOne);
        authority.setResourceOperations(resultOperations);
        return authority;
    }

    @Override
    @CacheEvict(cacheNames = {"AuthorityAttributes"}, key = "'all_resource'")
    @Transactional(rollbackFor = Exception.class)
    public void deleteResourceAuthoritiesByRoleName(String rolename) {
        if (authorityRepository.existsById(rolename)) {
            Authority authority = authorityRepository.getOne(rolename);
            authority.setResourceOperations(null);
            authorityRepository.save(authority);
        }
    }

    @Transactional(readOnly = true)
    @Override
    @Cacheable(cacheNames = "AuthorityAttributes", key = "'all_resource'", sync = true)
    public Collection<ResourceAuthorityAttribute> listAll() {
        List<Authority> authorities = authorityRepository.findAll();
        // 一个资源的权限配置
        final Map<UriResource, ResourceAuthorityAttribute> resourceAuthorityMap = new HashMap<>();
        
        authorities.forEach(authority -> authority.getResourceOperations().forEach((resource, operation) -> {
                for (HttpMethod method : methods) {
                    addAuthority(resourceAuthorityMap, resource, method, operation, authority.getRoleName());
                }
            })
        );
        return Collections.unmodifiableCollection(resourceAuthorityMap.values());
    }

    private void addAuthority(Map<UriResource, ResourceAuthorityAttribute> map,
                              AuthResource resource,
                              HttpMethod method,
                              ResourceOperation operation,
                              String authority) {
        UriResource ur = UriResource.of(method.toString(),
            resource.getMatcher(),
            resource.getMatchType(),
            resource.getScope());
        Boolean access = null;
        switch (method) {
            case POST:
                access = operation.getSaveable();
                break;
            case PUT:
                access = operation.getUpdateable();
                break;
            case GET:
                access = operation.getReadable();
                break;
            case DELETE:
                access = operation.getDeleteable();
                break;
            case PATCH:
                access = operation.getPatchable();
                break;
            default:
                break;
        }

        ResourceAuthorityAttribute raa = map.get(ur);
        if (Objects.isNull(raa)) {
            raa = new ResourceAuthorityAttribute(ur);
            map.put(ur, raa);
        }
        if (Boolean.TRUE.equals(access)) {
            raa.addAccessAuthority(authority);
        }
        if (Boolean.FALSE.equals(access)) {
            raa.addDenyAuthority(authority);
        }
    }
}
