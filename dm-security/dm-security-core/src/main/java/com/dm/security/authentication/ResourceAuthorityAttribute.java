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

    /**
     * 指示是否拒绝所有的访问
     */
    private boolean denyAll = true;

    /**
     * 是否必须授权访问
     */
    private boolean authenticated;

    public ResourceAuthorityAttribute(
        UriResource resource,
        boolean denyAll,
        boolean authenticated,
        Set<String> accessAuthorities,
        Set<String> denyAuthorities) {
        this.denyAll = denyAll;
        this.authenticated = authenticated;
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
        this.denyAll = false;
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

    /**
     * 是否授权用户即可访问，配置为false表示可以匿名访问
     *
     * @return
     */
    public boolean isAuthenticated() {
        return this.authenticated;
    }


    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
        if (authenticated) {
            this.denyAll = false;
        }
    }

    public void setDenyAll(boolean denyAll) {
        this.denyAll = denyAll;
        if (denyAll) {
            this.authenticated = false;
        }
    }


    public boolean isDenyAll() {
        return denyAll;
    }
}
