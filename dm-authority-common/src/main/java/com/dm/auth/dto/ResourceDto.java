package com.dm.auth.dto;

import com.dm.common.dto.IdentifiableDto;
import com.dm.security.authentication.UriResource.MatchType;
import org.springframework.http.HttpMethod;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ResourceDto implements Serializable, IdentifiableDto<Long> {

    private static final long serialVersionUID = -847613336378865468L;
    /**
     * 资源id
     */
    private Long id;
    /**
     * 资源名称
     */
    private String name;

    /**
     * 该资源的方法
     */
    private Set<HttpMethod> methods = new HashSet<>();

    /**
     * 资源匹配字符串
     */
    private String matcher;

    /**
     * 资源描述
     */
    private String description;

    /**
     * 资源Scope
     */
    private Set<String> scope = new HashSet<>();

    /**
     * 资源匹配方式
     */
    private MatchType matchType;

    /**
     * 允许访问角色信息
     */
    private Set<RoleDto> accessAuthorities = new HashSet<>();

    /**
     * 拒绝访问角色信息
     *
     * @return
     */
    private Set<RoleDto> denyAuthorities = new HashSet<>();

    public ResourceDto() {
    }

    public ResourceDto(Long id, String name, Set<HttpMethod> methods, String matcher, String description, Set<String> scope, MatchType matchType, Set<RoleDto> accessAuthorities, Set<RoleDto> denyAuthorities) {
        this.id = id;
        this.name = name;
        this.methods = methods;
        this.matcher = matcher;
        this.description = description;
        this.scope = scope;
        this.matchType = matchType;
        this.accessAuthorities = accessAuthorities;
        this.denyAuthorities = denyAuthorities;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<HttpMethod> getMethods() {
        return methods;
    }

    public void setMethods(Set<HttpMethod> methods) {
        this.methods = methods;
    }

    public String getMatcher() {
        return matcher;
    }

    public void setMatcher(String matcher) {
        this.matcher = matcher;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getScope() {
        return scope;
    }

    public void setScope(Set<String> scope) {
        this.scope = scope;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    public Set<RoleDto> getAccessAuthorities() {
        return accessAuthorities;
    }

    public void setAccessAuthorities(Set<RoleDto> accessAuthorities) {
        this.accessAuthorities = accessAuthorities;
    }

    public Set<RoleDto> getDenyAuthorities() {
        return denyAuthorities;
    }

    public void setDenyAuthorities(Set<RoleDto> denyAuthorities) {
        this.denyAuthorities = denyAuthorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceDto that = (ResourceDto) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(methods, that.methods) && Objects.equals(matcher, that.matcher) && Objects.equals(description, that.description) && Objects.equals(scope, that.scope) && matchType == that.matchType && Objects.equals(accessAuthorities, that.accessAuthorities) && Objects.equals(denyAuthorities, that.denyAuthorities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, methods, matcher, description, scope, matchType, accessAuthorities, denyAuthorities);
    }

    @Override
    public String toString() {
        return "ResourceDto{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", methods=" + methods +
            ", matcher='" + matcher + '\'' +
            ", description='" + description + '\'' +
            ", scope=" + scope +
            ", matchType=" + matchType +
            ", accessAuthorities=" + accessAuthorities +
            ", denyAuthorities=" + denyAuthorities +
            '}';
    }
}
