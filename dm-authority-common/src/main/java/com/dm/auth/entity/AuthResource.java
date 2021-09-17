package com.dm.auth.entity;

import com.dm.common.entity.AbstractEntity;
import com.dm.security.authentication.UriResource.MatchType;
import org.springframework.http.HttpMethod;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 定义资源
 *
 * @author LiDong
 */
@Entity
@Table(name = "dm_resource_")
public class AuthResource extends AbstractEntity {

    /**
     * 资源名称
     */
    @Column(name = "name_", length = 100, nullable = false)
    private String name;

    @ElementCollection
    @CollectionTable(name = "dm_resource_method_", joinColumns = {
        @JoinColumn(name = "resource_id_")
    }, foreignKey = @ForeignKey(name = "FK_dm_resource_method_dm_resource_id_"))
    @Enumerated(EnumType.STRING)
    private Set<HttpMethod> methods = new HashSet<>();

    /**
     * 资源匹配路径
     */
    @Column(name = "matcher_", length = 400, nullable = false)
    private String matcher;

    /**
     * 路径匹配类型
     */
    @Column(name = "match_type_", nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchType matchType = MatchType.ANT_PATH;

    /**
     * 资源所属范围
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "scope_")
    @CollectionTable(name = "dm_resource_scope_", joinColumns = {
        @JoinColumn(name = "resource_id_")
    }, indexes = {
        @Index(name = "IDX_dm_resource_scope_resource_", columnList = "resource_id_")
    }, foreignKey = @ForeignKey(name = "FK_dm_resource_scope_dm_resource_id_"))
    private Set<String> scope = new HashSet<>();

    public void setScope(Set<String> scope) {
        this.scope.clear();
        this.scope.addAll(scope);
    }

    /**
     * 资源描述
     */
    @Column(name = "description_", length = 800)
    private String description;


    /**
     * 允许访问角色信息
     */

    @ManyToMany
    @JoinTable(name = "dm_resource_access_authority_", joinColumns = {
        @JoinColumn(name = "resource_id_")
    }, inverseJoinColumns = {
        @JoinColumn(name = "role_id_")
    }, foreignKey = @ForeignKey(name = "dm_resource_access_authority_dm_resource_id_"),
        inverseForeignKey = @ForeignKey(name = "dm_resource_access_authority_dm_role_id_"))
    private Set<Role> accessAuthorities = new HashSet<>();

    public void setAccessAuthorities(Set<Role> accessAuthorities) {
        this.accessAuthorities.clear();
        this.accessAuthorities.addAll(accessAuthorities);
    }

    /**
     * 拒绝访问角色信息
     *
     * @return
     */
    @ManyToMany
    @JoinTable(name = "dm_resource_deny_authority_", joinColumns = {
        @JoinColumn(name = "resource_id_")
    }, inverseJoinColumns = {
        @JoinColumn(name = "role_id_")
    }, foreignKey = @ForeignKey(name = "dm_resource_deny_authority_dm_resource_id_"),
        inverseForeignKey = @ForeignKey(name = "dm_resource_deny_authority_role_id_"))
    private Set<Role> denyAuthorities = new HashSet<>();

    public void setDenyAuthorities(Set<Role> denyAuthorities) {
        this.denyAuthorities.clear();
        this.denyAuthorities.addAll(denyAuthorities);
    }

    public AuthResource() {
    }

    public AuthResource(String name, Set<HttpMethod> methods, String matcher, MatchType matchType, Set<String> scope, String description, Set<Role> accessAuthorities, Set<Role> denyAuthorities) {
        this.name = name;
        this.methods = methods;
        this.matcher = matcher;
        this.matchType = matchType;
        this.scope = scope;
        this.description = description;
        this.accessAuthorities = accessAuthorities;
        this.denyAuthorities = denyAuthorities;
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

    public MatchType getMatchType() {
        return matchType;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    public Set<String> getScope() {
        return scope;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Role> getAccessAuthorities() {
        return accessAuthorities;
    }

    public Set<Role> getDenyAuthorities() {
        return denyAuthorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AuthResource that = (AuthResource) o;
        return Objects.equals(name, that.name) && Objects.equals(methods, that.methods) && Objects.equals(matcher, that.matcher) && matchType == that.matchType && Objects.equals(scope, that.scope) && Objects.equals(description, that.description) && Objects.equals(accessAuthorities, that.accessAuthorities) && Objects.equals(denyAuthorities, that.denyAuthorities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, methods, matcher, matchType, scope, description, accessAuthorities, denyAuthorities);
    }

    @Override
    public String toString() {
        return "AuthResource{" +
            "name='" + name + '\'' +
            ", methods=" + methods +
            ", matcher='" + matcher + '\'' +
            ", matchType=" + matchType +
            ", scope=" + scope +
            ", description='" + description + '\'' +
            ", accessAuthorities=" + accessAuthorities +
            ", denyAuthorities=" + denyAuthorities +
            '}';
    }

}
