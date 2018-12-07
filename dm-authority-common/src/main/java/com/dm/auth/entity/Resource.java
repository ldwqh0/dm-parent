package com.dm.auth.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.dm.common.entity.AbstractEntity;
import com.dm.security.access.RequestAuthorityAttribute.MatchType;

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
	 * 资源名称
	 */
	@Column(name = "name_", length = 100, unique = true)
	private String name;

	@Column(name = "matcher_", length = 400, unique = true)
	private String matcher;

	@Column(name = "match_type_")
	@Enumerated(EnumType.STRING)
	private MatchType matchType = MatchType.ANT_PATH;

	@Column(name = "description_", length = 800)
	private String description;

}
