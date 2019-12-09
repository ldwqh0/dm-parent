package com.dm.security.authentication;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.Authentication;

/**
 * 一组资源授权配置，包括资源的匹配器，资源的scope，一个${@link Authentication}以及该组合是否可以通过
 * 
 * @author ldwqh0@outlook.com
 * @since 0.2.1
 *
 */
public class ResourceAuthorityAttribute {
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
     * 是否必须授权访问
     */
    private final boolean authenticated;

    public ResourceAuthorityAttribute(
            UriResource resource,
            Set<String> accessAuthorities,
            Set<String> denyAuthorities,
            boolean authenticated) {
        this.authenticated = authenticated;
        this.resource = resource;
        this.accessAuthorities = accessAuthorities;
        this.denyAuthorities = denyAuthorities;
    }

    public ResourceAuthorityAttribute(UriResource resource) {
        super();
        this.resource = resource;
        this.authenticated = true;
        this.accessAuthorities = new HashSet<String>();
        this.denyAuthorities = new HashSet<String>();
    }

    /**
     * 获取该组资源的授权信息
     * 
     * @return
     */
    public UriResource getResource() {
        return resource;
    }

    public Set<String> getDenyAuthorities() {
        return this.denyAuthorities;

    }

    public Set<String> getAccessAuthority() {
        return this.accessAuthorities;
    }

    public void addAccessAuthority(String authority) {
        this.accessAuthorities.add(authority);
    }

    public void addDenyAuthorities(String authroity) {
        this.denyAuthorities.add(authroity);
    }

    /**
     * 是否授权用户即可访问，不配置代表可以匿名访问
     * 
     * @return
     */
    public boolean isAuthenticated() {
        return this.authenticated;
    }

}
