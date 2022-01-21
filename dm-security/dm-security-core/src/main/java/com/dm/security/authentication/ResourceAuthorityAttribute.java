package com.dm.security.authentication;

import com.dm.collections.CollectionUtils;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

/**
 * 一组资源授权配置，包括资源的匹配器，资源的scope，一个${@link Authentication}以及该组合是否可以通过
 *
 * @author ldwqh0@outlook.com
 * @since 0.2.1
 */
public class ResourceAuthorityAttribute implements Serializable {

    private static final long serialVersionUID = -5073142461362078685L;

    /**
     * 资源的匹配器
     */
    private final UriResource resource;


    /**
     * 允许访问角色信息
     */
    private final Set<String> accessAuthorities = new HashSet<>();

    /**
     * 拒绝访问角色信息
     */
    private final Set<String> denyAuthorities = new HashSet<>();


    public ResourceAuthorityAttribute(
        UriResource resource,
        Set<String> accessAuthorities,
        Set<String> denyAuthorities) {
        this.resource = resource;
        if (CollectionUtils.isNotEmpty(accessAuthorities)) {
            this.accessAuthorities.addAll(accessAuthorities);
        }
        if (CollectionUtils.isNotEmpty(denyAuthorities)) {
            this.denyAuthorities.addAll(denyAuthorities);
        }
    }

    public ResourceAuthorityAttribute(UriResource resource) {
        this(resource, null, null);
    }

    /**
     * 获取该组资源的授权信息
     *
     */
    public UriResource getResource() {
        return resource;
    }

    /**
     * 拒绝访问的角色
     *
     */
    public Set<String> getDenyAuthorities() {
        return unmodifiableSet(this.denyAuthorities);
    }

    /**
     * 允许访问的角色
     *
     */
    public Set<String> getAccessAuthorities() {
        return unmodifiableSet(this.accessAuthorities);
    }

    /**
     * 添加一个允许访问的角色
     *
     * @param authority 允许访问的角色
     */
    public void addAccessAuthority(String authority) {
        this.accessAuthorities.add(authority);
        this.denyAuthorities.remove(authority);
    }

    /**
     * 添加一个阻止访问的角色
     *
     * @param authority 禁止访问的角色
     */
    public void addDenyAuthority(String authority) {
        this.denyAuthorities.add(authority);
        this.accessAuthorities.remove(authority);
    }

}
