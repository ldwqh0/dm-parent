package com.dm.auth.service.impl;

import com.dm.auth.converter.RoleConverter;
import com.dm.auth.dto.MenuAuthorityDto;
import com.dm.auth.dto.MenuDto;
import com.dm.auth.dto.ResourceAuthorityDto;
import com.dm.auth.dto.RoleDto;
import com.dm.auth.entity.*;
import com.dm.auth.entity.Role.Status;
import com.dm.auth.repository.MenuRepository;
import com.dm.auth.repository.ResourceRepository;
import com.dm.auth.repository.RoleRepository;
import com.dm.auth.service.RoleService;
import com.dm.collections.Maps;
import com.dm.common.exception.DataNotExistException;
import com.dm.security.authentication.ResourceAuthorityAttribute;
import com.dm.security.authentication.ResourceAuthorityService;
import com.dm.security.authentication.UriResource;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService, ResourceAuthorityService {

    private final RoleConverter roleConverter;

    private final RoleRepository roleRepository;

    private final MenuRepository menuRepository;

    private final ResourceRepository resourceRepository;

    private final QRole qRole = QRole.role;

    // 进行请求限定的请求类型
    private final HttpMethod[] methods = {
        HttpMethod.GET,
        HttpMethod.POST,
        HttpMethod.PUT,
        HttpMethod.DELETE,
        HttpMethod.PATCH,
        HttpMethod.HEAD,
        HttpMethod.OPTIONS,
    };

    @Override
    public boolean exist() {
        return roleRepository.count() > 0;
    }

    @Override
    public Optional<Role> findByFullname(String authority) {
        String[] groupRole = authority.split("\\_", 2);
        return roleRepository.findByGroupAndName(groupRole[0], groupRole[1]);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    @CacheEvict(cacheNames = {"AuthorityMenus", "AuthorityAttributes"}, allEntries = true)
    public Role save(RoleDto roleDto) {
        return roleRepository.save(roleConverter.copyProperties(new Role(), roleDto));
    }

    @Override
    public boolean nameExist(Long id, String group, String name) {
        BooleanBuilder builder = new BooleanBuilder();
        if (id != null) {
            builder.and(qRole.id.ne(id));
        }
        builder.and(qRole.group.eq(group));
        builder.and(qRole.name.eq(name));
        return roleRepository.exists(builder);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    @CacheEvict(cacheNames = {"AuthorityMenus", "AuthorityAttributes"}, allEntries = true)
    public Role update(long id, RoleDto roleDto) {
        return roleRepository.save(roleConverter.copyProperties(roleRepository.getOne(id), roleDto));
    }

    @Override
    public Optional<Role> get(long id) {
        return roleRepository.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    @CacheEvict(cacheNames = {"AuthorityMenus", "AuthorityAttributes"}, allEntries = true)
    public void delete(long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public Page<Role> search(String group, String key, Pageable pageable) {
        BooleanBuilder query = new BooleanBuilder();
        if (Objects.nonNull(group)) {
            query.and(qRole.group.eq(group));
        }
        if (StringUtils.isNotBlank(key)) {
            query.and(qRole.name.containsIgnoreCase(key).or(qRole.description.containsIgnoreCase(key)));
            return roleRepository.findAll(query, pageable);
        }
        return roleRepository.findAll(query, pageable);
    }

    @Override
    public List<Role> listAllEnabled() {
        return roleRepository.findByState(Status.ENABLED);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = "AuthorityMenus", allEntries = true)
    public Role saveAuthority(Long roleId, MenuAuthorityDto authorityDto) {
        Role authority;
        if (roleRepository.existsById(roleId)) {
            authority = roleRepository.getOne(roleId);
            Set<MenuDto> menus = authorityDto.getAuthorityMenus();
            Set<Menu> list = menus.stream().map(menuRepository::getByDto).collect(Collectors.toSet());
            authority.setMenus(list);
            return authority;
        } else {
            throw new DataNotExistException("指定角色不存在");
        }

    }

    /**
     * 根据角色查询菜单项目
     *
     * @param authority 角色名称
     * @return 角色的授权菜单列表
     */
    @Override
    @Cacheable(cacheNames = "AuthorityMenus", sync = true)
    @Transactional(readOnly = true)
    public Set<Menu> findAuthorityMenus(String authority) {
        Set<Menu> parents = new HashSet<>();
        Set<Menu> menus = findByFullname(authority).map(Role::getMenus)
            .orElseGet(Collections::emptySet);
        // 递归添加所有父级菜单
        for (Menu menu : menus) {
            addParent(menu, parents);
        }
        menus.addAll(parents);
        menus.removeIf(this::isDisabled);
        return menus;
    }

    @Override
    public List<String> listGroups() {
        return roleRepository.listGroups();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = {"AuthorityAttributes"}, key = "'all_resource'")
    public Role saveAuthority(ResourceAuthorityDto authorityDto) {
        Long roleId = authorityDto.getRoleId();
        if (roleRepository.existsById(roleId)) {
            Role role = roleRepository.getOne(roleId);
            Map<AuthResource, ResourceOperation> resultOperations = Maps
                .transformKeys(authorityDto.getResourceAuthorities(), resourceRepository::getOne);
            role.setResourceOperations(resultOperations);
            return roleRepository.save(role);
        } else {
            throw new DataNotExistException("指定角色不存在");
        }
    }


    @Override
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
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

    private boolean isDisabled(Menu menu) {
        return !isEnabled(menu);
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

    @Transactional(readOnly = true)
    @Override
    @Cacheable(cacheNames = "AuthorityAttributes", key = "'all_resource'", sync = true)
    public Collection<ResourceAuthorityAttribute> listAll() {
        List<Role> roles = roleRepository.findAll();
        // 一个资源的权限配置
        final Map<UriResource, ResourceAuthorityAttribute> resourceAuthorityMap = new HashMap<>();

        roles.forEach(role -> role.getResourceOperations().forEach((resource, operation) -> {
            for (HttpMethod method : methods) {
                addAuthority(resourceAuthorityMap, resource, method, operation, role.getFullName());
            }
        }));
        return Collections.unmodifiableCollection(resourceAuthorityMap.values());
    }

    private void addAuthority(Map<UriResource, ResourceAuthorityAttribute> map, AuthResource resource,
                              HttpMethod method, ResourceOperation operation, String authority) {
        UriResource ur = UriResource.of(method.toString(), resource.getMatcher(), resource.getMatchType(),
            resource.getScope());
        Boolean access = null;
        switch (method) {
            case POST:
                access = operation.getPostAble();
                break;
            case PUT:
                access = operation.getPutAble();
                break;
            case GET:
                access = operation.getGetAble();
                break;
            case DELETE:
                access = operation.getDeleteAble();
                break;
            case PATCH:
                access = operation.getPatchAble();
                break;
            case HEAD:
                access = operation.getHeadAble();
                break;
            case OPTIONS:
                access = operation.getOptionsAble();
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

    @Override
    public boolean existsByFullname(String authority) {
        String[] groupName = authority.split("\\_", 2);
        return roleRepository.existsByGroupAndName(groupName[0], groupName[1]);
    }

}
