package com.dm.auth.entity;

import com.dm.common.entity.AbstractEntity;
import com.dm.security.authentication.UriResource.MatchType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 定义资源
 *
 * @author LiDong
 */
@Entity
@Getter
@Setter
@Table(name = "dm_resource_")
public class AuthResource extends AbstractEntity {

    /**
     * 资源名称
     */
    @Column(name = "name_", length = 100, nullable = false)
    private String name;

    @ElementCollection
    @CollectionTable(name = "dm_resource_method_", joinColumns = {
        @JoinColumn(name = "dm_resource_id_")
    })
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
        @JoinColumn(name = "resource_", foreignKey = @ForeignKey(name = "FK_dm_resource_scope_resource_"))
    }, indexes = {
        @Index(name = "IDX_dm_resource_scope_resource_", columnList = "resource_")
    })
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
        @JoinColumn(name = "dm_resource_id_")
    })
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
        @JoinColumn(name = "dm_resource_id_")
    })
    private Set<Role> denyAuthorities = new HashSet<>();

    public void setDenyAuthorities(Set<Role> denyAuthorities) {
        this.denyAuthorities.clear();
        this.denyAuthorities.addAll(denyAuthorities);
    }

    /**
     * 指示是否拒绝所有的访问
     */
    @Column(name = "deny_all_")
    private boolean denyAll = true;

    public void setDenyAll(boolean denyAll) {
        this.denyAll = denyAll;
        if (denyAll) {
            this.authenticated = false;
        }
    }

    /**
     * 是否必须授权访问
     */
    @Column(name = "authenticated_")
    private boolean authenticated;

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
        if (authenticated) {
            this.denyAll = false;
        }
    }
}
