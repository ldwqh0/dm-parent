package com.dm.uap.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.dm.common.entity.AbstractEntity;

/**
 * 定义资源
 * 
 * @author LiDong
 *
 */
@Entity(name = "dm_resource_")
public class Resource extends AbstractEntity {
	private static final long serialVersionUID = 8273786895229540103L;

	@Column(name = "matcher_", length = 400)
	public String matcher;

	@Column(name = "description_", length = 800)
	public String description;
}
