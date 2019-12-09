package com.dm.auth.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import org.springframework.security.web.util.matcher.RegexRequestMatcher;
//import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dm.auth.converter.ResourceOperationConverter;
import com.dm.auth.dto.MenuAuthorityDto;
import com.dm.auth.dto.MenuDto;
import com.dm.auth.dto.ResourceAuthorityDto;
import com.dm.auth.dto.ResourceOperationDto;
import com.dm.auth.entity.Authority;
import com.dm.auth.entity.Menu;
import com.dm.auth.entity.Resource;
import com.dm.auth.entity.Resource.MatchType;
import com.dm.auth.entity.ResourceOperation;
import com.dm.auth.repository.AuthorityRepository;
import com.dm.auth.repository.MenuRepository;
import com.dm.auth.repository.ResourceRepository;
import com.dm.auth.service.AuthorityService;
import com.dm.security.authentication.ResourceAuthorityAttribute;
import com.dm.security.authentication.ResourceAuthorityService;

import lombok.Data;

@Service("authorityService")
public class AuthorityServiceImpl implements AuthorityService, ResourceAuthorityService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private ResourceOperationConverter resourceOperationConverter;

    @Autowired
    private ResourceRepository resourceReopsitory;

    @Override
    @Transactional
    public Authority save(MenuAuthorityDto authorityDto) {
        Long roleId = authorityDto.getRoleId();
        String roleName = authorityDto.getRoleName();
        Authority authority = null;
        if (authorityRepository.existsById(roleName)) {
            authority = authorityRepository.getOne(roleName);
        } else {
            authority = new Authority();
            authority.setId(roleId);
            authority.setRoleName(roleName);
            authority = authorityRepository.save(authority);
        }
        List<MenuDto> menus = authorityDto.getAuthorityMenus();
        Set<Menu> list = menus.stream().map(MenuDto::getId).map(menuRepository::getOne).collect(Collectors.toSet());
        authority.setMenus(list);
        return authority;
    }

    /**
     * 获取指定角色列表的菜单项目，这个事务是只读的
     */
    @Override
    @Transactional(readOnly = true)
    public List<Menu> listMenuByAuthorities(List<String> ids) {
        // 添加所有菜单项
        // 所有菜单的父级菜单
        Set<Menu> parents = new HashSet<Menu>();
        Set<Menu> menus = ids.stream().filter(authorityRepository::existsById)
                .map(authorityRepository::getOne)
                .map(Authority::getMenus)
                .flatMap(Set::stream)
                .filter(Menu::isEnabled)
                .collect(Collectors.toSet());

        // 递归添加所有父级菜单
        for (Menu menu : menus) {
            addParent(menu, parents);
        }
        menus.addAll(parents);

        Iterator<Menu> menusIteraotr = menus.iterator();
        // 移除所有的不可用的项
        while (menusIteraotr.hasNext()) {
            Menu next = menusIteraotr.next();
            if (!isEnabled(next)) {
                menusIteraotr.remove();
            }
        }

        List<Menu> result = new ArrayList<Menu>();
        result.addAll(menus);
        Collections.sort(result, (o1, o2) -> (int) (o1.getOrder() - o2.getOrder()));
        return result;
    }

    /**
     * 循环的将某个菜单的父级菜单添加到指定列表
     * 
     * @param menu
     * @param collection
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
     * @param menu
     * @return
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
    public Optional<Authority> get(String rolename) {
        return authorityRepository.findById(rolename);
    }

    @Override
    public boolean exists() {
        return authorityRepository.count() > 0;
    }

    @Override
    @Transactional
    public Authority save(ResourceAuthorityDto authorityDto) {
        Long roleId = authorityDto.getRoleId();
        String roleName = authorityDto.getRoleName();
        Authority authority = null;
        if (authorityRepository.existsById(roleName)) {
            authority = authorityRepository.getOne(roleName);
        } else {
            authority = new Authority();
            authority.setId(roleId);
            authority.setRoleName(roleName);
            authority = authorityRepository.save(authority);
        }
        List<ResourceOperationDto> _resourceOperations = authorityDto.getResourceAuthorities();

        // 保存资源权限配置
        Set<ResourceOperation> resourceOperations = _resourceOperations.stream()
                // 如果某组配置全部为空，代表该组未配置，则删除该组配置
                .filter(this::hasAuhtority)
                .map(m -> {
                    ResourceOperation operation = new ResourceOperation();
                    resourceOperationConverter.copyProperties(operation, m);
                    operation.setResource(resourceReopsitory.getOne(m.getResource().getId()));
                    return operation;
                }).collect(Collectors.toSet());
        authority.setResourceOperations(resourceOperations);
        return authority;
    }

    @Override
    public void deleteResourceAuthoritiesByRoleName(String rolename) {
        if (authorityRepository.existsById(rolename)) {
            Authority authority = authorityRepository.getOne(rolename);
            Set<ResourceOperation> operations = authority.getResourceOperations();
            if (CollectionUtils.isNotEmpty(operations)) {
                operations.clear();
            }
        }
    }

    // TODO 这里要使用缓存策略
    @Transactional(readOnly = true)
    @Override
    @Cacheable(cacheNames = "AuthorityAttribute", key = "all", sync = true)
    public List<ResourceAuthorityAttribute> listAll() {
        List<Authority> authorities = authorityRepository.findAll();
        List<ResourceAuthorityAttribute> attributes = new ArrayList<>();
//        RequestMatchHolder rmh = new RequestMatchHolder();
        for (Authority authority : authorities) {
            Set<ResourceOperation> operations = authority.getResourceOperations();
            for (ResourceOperation operation : operations) {
                Resource r = operation.getResource();
                Boolean saveable = operation.getSaveable();
                Boolean readable = operation.getReadable();
                Boolean updateable = operation.getUpdateable();
                Boolean deleteable = operation.getDeleteable();

//                if (!Objects.isNull(saveable)) {
//                    attributes.add(new RequestAuthorityAttribute(
//                            rmh.getRequestMatcher(r, HttpMethod.POST),
//                            authority.getRoleName(), r.getScope(),
//                            saveable));
//                }
//
//                if (!Objects.isNull(readable)) {
//                    attributes.add(new RequestAuthorityAttribute(
//                            rmh.getRequestMatcher(r, HttpMethod.GET),
//                            authority.getRoleName(), r.getScope(),
//                            readable));
//                }
//                if (!Objects.isNull(updateable)) {
//                    attributes.add(new RequestAuthorityAttribute(
//                            rmh.getRequestMatcher(r, HttpMethod.PUT),
//                            authority.getRoleName(), r.getScope(),
//                            updateable));
//                }
//                if (!Objects.isNull(deleteable)) {
//                    attributes.add(new RequestAuthorityAttribute(
//                            rmh.getRequestMatcher(r, HttpMethod.DELETE),
//                            authority.getRoleName(),
//                            r.getScope(),
//                            deleteable));
//                }
            }
        }

        return attributes;

    }

    /**
     * 判断一组资源操作是否被配置<br />
     * 如果所有的配置都为空，则判定为未配置
     * 
     * @param op
     * @return
     */
    private boolean hasAuhtority(ResourceOperationDto op) {
        return !Objects.isNull(op.getDeleteable()) ||
                !Objects.isNull(op.getReadable()) ||
                !Objects.isNull(op.getSaveable()) ||
                !Objects.isNull(op.getUpdateable());
    }

}

//class RequestMatchHolder {
//    private final ConcurrentHashMap<RescoureKey, RequestMatcher> cache = new ConcurrentHashMap<>();
//
//    /**
//     * 根据资源和请求类型，获取RequestMatcher
//     * 
//     * @param resource
//     * @param method
//     * @return
//     */
//    public RequestMatcher getRequestMatcher(Resource resource, HttpMethod method) {
//        RescoureKey key = new RescoureKey(resource, method);
//        RequestMatcher rm = null;
//        if (Objects.isNull(rm = cache.get(key))) {
//            rm = buildRequestMatcher(resource, method);
//            cache.put(key, rm);
//        }
//        return rm;
//    }
//
//    private RequestMatcher buildRequestMatcher(Resource resource, HttpMethod method) {
//        MatchType type = resource.getMatchType();
//        String path = resource.getMatcher();
//        switch (type) {
//        case ANT_PATH:
//            return new AntPathRequestMatcher(path, method.toString());
//        case REGEXP:
//            return new RegexRequestMatcher(path, method.toString());
//        default:
//            throw new RuntimeException("Un supported matchType");
//        }
//    }
//
//    @Data
//    static final class RescoureKey {
//        private final Resource resource;
//        private final HttpMethod method;
//    }
//}
