package com.dm.uap.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.dm.common.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "dm_department_")
@Getter
@Setter
public class Department extends AbstractEntity {
	private static final long serialVersionUID = -6824250546678747271L;

	/**
	 * 部门名称
	 */
	@Column(name = "full_name_", length = 100, nullable = false, unique = true)
	private String fullname;

	@Column(name = "short_name_", length = 100)
	private String shortname;

	/**
	 * 上级部门
	 */
	@ManyToOne
	@JoinColumn(name = "parent_id_")
	private Department parent;

	/**
	 * 部门描述
	 */
	@Column(name = "description_", length = 2000)
	private String description;

	@Column(name = "order_")
	private Long order;
}
