package com.dm.auth.dto;

import com.dm.collections.CollectionUtils;
import com.dm.common.dto.IdentifiableDto;
import com.dm.security.authentication.UriResource.MatchType;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpMethod;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.dm.collections.Sets.hashSet;

public class ResourceDto implements Serializable, IdentifiableDto<Long> {

    private static final long serialVersionUID = -847613336378865468L;
    /**
     * 资源id
     */
    private final Long id;
    /**
     * 资源名称
     */
    private final String name;

    /**
     * 该资源的方法
     */
    private final Set<HttpMethod> methods = new HashSet<>();

    /**
     * 资源匹配字符串
     */
    private final String matcher;

    /**
     * 资源描述
     */
    private final String description;

    /**
     * 资源Scope
     */
    private final Set<String> scope = new HashSet<>();

    /**
     * 资源匹配方式
     */
    private final MatchType matchType;

    /**
     * 允许访问角色信息
     */
    private final Set<RoleDto> accessAuthorities = new HashSet<>();

    /**
     * 拒绝访问角色信息
     */
    private final Set<RoleDto> denyAuthorities = new HashSet<>();


    public ResourceDto(
        @JsonProperty("id") Long id,
        @JsonProperty("name") String name,
        @JsonProperty("methods") Set<HttpMethod> methods,
        @JsonProperty("matcher") String matcher,
        @JsonProperty("description") String description,
        @JsonProperty("scope") Set<String> scope,
        @JsonProperty("matchType") MatchType matchType,
        @JsonProperty("accessAuthorities") Set<RoleDto> accessAuthorities,
        @JsonProperty("denyAuthorities") Set<RoleDto> denyAuthorities
    ) {
        this.id = id;
        this.name = name;
        if (CollectionUtils.isNotEmpty(methods)) {
            this.methods.addAll(methods);
        }
        this.matcher = matcher;
        this.description = description;
        if (CollectionUtils.isNotEmpty(scope)) {
            this.scope.addAll(scope);
        }
        if (Objects.isNull(matchType)) {
            this.matchType = MatchType.ANT_PATH;
        } else {
            this.matchType = matchType;
        }
        if (CollectionUtils.isNotEmpty(accessAuthorities)) {
            this.accessAuthorities.addAll(accessAuthorities);
        }
        if (CollectionUtils.isNotEmpty(denyAuthorities)) {
            this.denyAuthorities.addAll(denyAuthorities);
        }
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<HttpMethod> getMethods() {
        return hashSet(methods);
    }

    public String getMatcher() {
        return matcher;
    }

    public String getDescription() {
        return description;
    }

    public Set<String> getScope() {
        return hashSet(scope);
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public Set<RoleDto> getAccessAuthorities() {
        return hashSet(accessAuthorities);
    }

    public Set<RoleDto> getDenyAuthorities() {
        return hashSet(denyAuthorities);
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
