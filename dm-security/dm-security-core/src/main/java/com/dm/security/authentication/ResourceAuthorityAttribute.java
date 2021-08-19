package com.dm.security.authentication;

import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
    private final Set<String> accessAuthorities;

    /**
     * 拒绝访问角色信息
     *
     * @return
     */
    private final Set<String> denyAuthorities;


    public ResourceAuthorityAttribute(
        UriResource resource,
        Set<String> accessAuthorities,
        Set<String> denyAuthorities) {
        this.resource = resource;
        this.accessAuthorities = accessAuthorities;
        this.denyAuthorities = denyAuthorities;
    }

    public ResourceAuthorityAttribute(UriResource resource) {
        super();
        this.resource = resource;
        this.accessAuthorities = new HashSet<>();
        this.denyAuthorities = new HashSet<>();
    }

    /**
     * 获取该组资源的授权信息
     *
     * @return
     */
    public UriResource getResource() {
        return resource;
    }

    /**
     * 拒绝访问的角色
     *
     * @return
     */
    public Set<String> getDenyAuthorities() {
        return this.denyAuthorities;
    }

    /**
     * 允许访问的角色
     *
     * @return
     */
    public Set<String> getAccessAuthorities() {
        return this.accessAuthorities;
    }

    /**
     * 添加一个允许访问的角色
     *
     * @param authority
     */
    public void addAccessAuthority(String authority) {
        this.accessAuthorities.add(authority);
    }

    /**
     * 添加一个阻止访问的角色
     *
     * @param authority
     */
    public void addDenyAuthority(String authority) {
        this.denyAuthorities.add(authority);
        this.accessAuthorities.remove(authority);
    }

}
