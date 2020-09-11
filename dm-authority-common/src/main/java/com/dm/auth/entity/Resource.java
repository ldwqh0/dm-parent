package com.dm.auth.entity;

import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.dm.common.entity.AbstractEntity;
import com.dm.security.authentication.UriResource.MatchType;

import lombok.Getter;
import lombok.Setter;

/**
 * 定义资源
 * 
 * @author LiDong
 *
 */
@Entity
@Getter
@Setter
@Table(name = "dm_resource_")
public class Resource extends AbstractEntity {

    /**
     * 资源名称
     */
    @Column(name = "name_", length = 100, unique = true, nullable = false)
    private String name;

    /**
     * 资源匹配路径
     */
    @Column(name = "matcher_", length = 400, unique = true, nullable = false)
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
            @JoinColumn(name = "resource_")
    })
    private Set<String> scope;

    /**
     * 资源描述
     */
    @Column(name = "description_", length = 800)
    private String description;

}
