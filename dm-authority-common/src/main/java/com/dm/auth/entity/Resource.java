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

import com.dm.common.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 定义资源
 * 
 * @author LiDong
 *
 */
@Entity(name = "dm_resource_")
@Getter
@Setter
public class Resource extends AbstractEntity {
	private static final long serialVersionUID = 8273786895229540103L;

	/**
     * 资源的匹配模式
     * 
     * @author LiDong
     *
     */
    public enum MatchType {
        /**
         * 路径匹配
         */
        ANT_PATH,
        /**
         * 正则表达式匹配
         */
        REGEXP,
    }
	
	/**
	 * 资源名称
	 */
	@Column(name = "name_", length = 100, unique = true)
	private String name;

	/**
	 * 资源匹配路径
	 */
	@Column(name = "matcher_", length = 400, unique = true)
	private String matcher;

	/**
	 * 路径匹配类型
	 */
	@Column(name = "match_type_")
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
