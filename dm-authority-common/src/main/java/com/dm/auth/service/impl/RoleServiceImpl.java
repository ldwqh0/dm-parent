package com.dm.auth.service.impl;

import com.dm.auth.converter.MenuConverter;
import com.dm.auth.converter.RoleConverter;
import com.dm.auth.dto.MenuAuthorityDto;
import com.dm.auth.dto.MenuDto;
import com.dm.auth.dto.RoleDto;
import com.dm.auth.entity.Menu;
import com.dm.auth.entity.QRole;
import com.dm.auth.entity.Role;
import com.dm.auth.entity.Role.Status;
import com.dm.auth.repository.MenuRepository;
import com.dm.auth.repository.RoleRepository;
import com.dm.auth.service.RoleService;
import com.dm.common.exception.DataNotExistException;
import com.querydsl.core.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final MenuRepository menuRepository;

    private final QRole qRole = QRole.role;

    private final EntityManager entityManager;

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

    public RoleServiceImpl(RoleRepository roleRepository, MenuRepository menuRepository, EntityManager entityManager) {
        this.roleRepository = roleRepository;
        this.menuRepository = menuRepository;
        this.entityManager = entityManager;
    }

    @Override
    public boolean exist() {
        return roleRepository.findMaxId().isPresent();
    }

    @Override
    public Optional<RoleDto> findByFullName(String authority) {
        String[] groupRole = authority.split("\\_", 2);
        return roleRepository.findByGroupAndName(groupRole[0], groupRole[1]).map(RoleConverter::toDto);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    @CacheEvict(cacheNames = {"AuthorityMenus", "AuthorityAttributes"}, allEntries = true)
    public RoleDto save(RoleDto roleDto) {
        return RoleConverter.toDto(roleRepository.save(copyProperties(new Role(), roleDto)));
    }

    @Override
    @Transactional
    public RoleDto saveWithId(RoleDto roleDto) {
        if (roleRepository.existsById(roleDto.getId())) {
            throw new RuntimeException("the role with give id has been exist");
        } else {
            entityManager.persist(new PrimaryRole(
                roleDto.getId(),
                roleDto.getName(),
                roleDto.getGroup(),
                roleDto.getDescription()
            ));
            return roleDto;
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    @CacheEvict(cacheNames = {"AuthorityMenus", "AuthorityAttributes"}, allEntries = true)
    public Role update(long id, RoleDto roleDto) {
        return roleRepository.save(copyProperties(roleRepository.getById(id), roleDto));
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
            query.and(qRole.name.containsIgnoreCase(key)
                .or(qRole.description.containsIgnoreCase(key))
                .or(qRole.group.containsIgnoreCase(key))
            );
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
    public MenuAuthorityDto saveAuthority(Long roleId, Set<MenuDto> menus) {
        Role authority;
        if (roleRepository.existsById(roleId)) {
            authority = roleRepository.getById(roleId);
            Set<Menu> list = menus.stream().map(menuRepository::getByDto).collect(Collectors.toSet());
            authority.setMenus(list);
            return RoleConverter.toMenuAuthorityDto(roleRepository.save(authority));
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
    @Cacheable(cacheNames = "AuthorityMenus", sync = true, key = "#p0+'_p_'+#p1")
    @Transactional(readOnly = true)
    public Set<MenuDto> findAuthorityMenus(String authority, final Long root) {
        String[] groupRole = authority.split("\\_", 2);
        Set<Menu> parents = new HashSet<>();
        Set<Menu> menus = roleRepository.findByGroupAndName(groupRole[0], groupRole[1]).map(Role::getMenus).orElseGet(Collections::emptySet);
        // 递归添加所有父级菜单
        menus.forEach(menu -> addParent(menu, parents));
        menus.addAll(parents);
        return menus.stream()
            // 只查找启用的项目
            .filter(this::isEnabled)
            // 如果root是空，不做过滤
            // 如果root不是空，则查找root的子孙代
            .filter(menu -> Objects.isNull(root) || (!Objects.equals(menu.getId(), root) && this.isOffspringOf(menu, root))) // 只需要是
            .map(it -> MenuConverter.toDto(it, null))
            .collect(Collectors.toSet());
    }

    @Override
    public List<String> listGroups() {
        return roleRepository.listGroups();
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
        menu.getParent().ifPresent(it -> {
            collection.add(it);
            addParent(it, collection);
        });
    }

    /**
     * 判断某个菜单是否被禁用，判断的标准是依次判断父级菜单是否被禁用，如果某个菜单的父级菜单被禁用，那么该菜单的子菜单也是被禁用的
     *
     * @param menu 要判断的菜单项
     * @return 返回是否会被禁用
     */
    private boolean isEnabled(Menu menu) {
        return menu.isEnabled() && menu.getParent().map(this::isEnabled).orElse(true);
    }

    /**
     * 判断给定菜单menu是不是指定菜单parentId的儿孙代
     *
     * @param menu 要判定的菜单,不能为空
     * @param root 要判断是否为parentId的子菜单
     * @return 判定结果
     */
    private boolean isOffspringOf(Menu menu, Long root) {
        return menu.getParent().map(it -> Objects.equals(it.getId(), root) || isOffspringOf(it, root))
            .orElseGet(() -> Objects.isNull(root));
    }

    @Override
    public boolean existsByFullName(String group, String name) {
        return roleRepository.existsByGroupAndName(group, name);
    }

    @Override
    public boolean existsByFullName(String group, String name, Long exclude) {
        if (Objects.isNull(exclude)) {
            return roleRepository.existsByGroupAndName(group, name);
        } else {
            return roleRepository.existsByGroupAndNameAndIdNot(group, name, exclude);
        }
    }

    private Role copyProperties(Role role, RoleDto roleDto) {
        if (role != null && roleDto != null) {
            role.setName(roleDto.getName());
            role.setDescription(roleDto.getDescription());
            role.setState(roleDto.getState());
            role.setGroup(roleDto.getGroup());
        }
        return role;
    }
}

/**
 * 这个类仅仅用户带id保存
 */
@Entity
@Table(name = "dm_role_")
class PrimaryRole {

    @Id
    @Column(name = "id_")
    private Long id;

    @Column(name = "name_", length = 100, nullable = false)
    private String name;

    @Column(name = "group_", length = 100, nullable = false)
    private String group;

    @Column(name = "state_", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private Status state = Status.ENABLED;

    @Column(name = "description_", length = 2000)
    private String description;

    @Version
    @Column(name = "version_", nullable = false)
    private Long version = 0L;

    public PrimaryRole() {
    }

    public PrimaryRole(Long id, String name, String group, String description) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.description = description;
    }

}
