package com.dm.auth.dto;

import com.dm.collections.CollectionUtils;
import com.dm.data.domain.Identifiable;
import com.dm.security.authentication.UriResource.MatchType;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpMethod;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

public class ResourceDto implements Serializable, Identifiable<Long> {

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

    private ResourceDto(@JsonProperty("id") Long id,
                        @JsonProperty("name") String name,
                        @JsonProperty("methods") Set<HttpMethod> methods,
                        @JsonProperty("matcher") String matcher,
                        @JsonProperty("description") String description,
                        @JsonProperty("scope") Set<String> scope,
                        @JsonProperty("matchType") MatchType matchType,
                        @JsonProperty("accessAuthorities") Set<RoleDto> accessAuthorities,
                        @JsonProperty("denyAuthorities") Set<RoleDto> denyAuthorities) {
        this.id = id;
        this.name = name;
        CollectionUtils.addAll(this.methods, methods);
        this.matcher = matcher;
        this.description = description;
        CollectionUtils.addAll(this.scope, scope);
        if (Objects.isNull(matchType)) {
            this.matchType = MatchType.ANT_PATH;
        } else {
            this.matchType = matchType;
        }
        CollectionUtils.addAll(this.accessAuthorities, accessAuthorities);
        CollectionUtils.addAll(this.denyAuthorities, denyAuthorities);
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<HttpMethod> getMethods() {
        return unmodifiableSet(methods);
    }

    public String getMatcher() {
        return matcher;
    }

    public String getDescription() {
        return description;
    }

    public Set<String> getScope() {
        return unmodifiableSet(scope);
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public Set<RoleDto> getAccessAuthorities() {
        return unmodifiableSet(accessAuthorities);
    }

    public Set<RoleDto> getDenyAuthorities() {
        return unmodifiableSet(denyAuthorities);
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

    public static Builder builder() {
        return new Builder();
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

    public static final class Builder {
        private Long id;
        private String name;
        private Set<HttpMethod> methods = new HashSet<>();
        private String matcher;
        private String description;
        private Set<String> scope = new HashSet<>();
        private MatchType matchType;
        private Set<RoleDto> accessAuthorities = new HashSet<>();
        private Set<RoleDto> denyAuthorities = new HashSet<>();

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder methods(Set<HttpMethod> methods) {
            this.methods = methods;
            return this;
        }

        public Builder matcher(String matcher) {
            this.matcher = matcher;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder scope(Set<String> scope) {
            this.scope = scope;
            return this;
        }

        public Builder matchType(MatchType matchType) {
            this.matchType = matchType;
            return this;
        }

        public Builder accessAuthorities(Set<RoleDto> accessAuthorities) {
            this.accessAuthorities = accessAuthorities;
            return this;
        }

        public Builder denyAuthorities(Set<RoleDto> denyAuthorities) {
            this.denyAuthorities = denyAuthorities;
            return this;
        }

        public ResourceDto build() {
            return new ResourceDto(id, name, methods, matcher, description, scope, matchType, accessAuthorities, denyAuthorities);
        }
    }
}
