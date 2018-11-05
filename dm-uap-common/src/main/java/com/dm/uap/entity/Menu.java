package com.dm.uap.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.dm.common.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "dm_menu_")
@Table(indexes = { @Index(columnList = "parent_", name = "IDX_bf_menu_parent_") })
public class Menu extends AbstractEntity {

	private static final long serialVersionUID = -5469864417544946812L;

	/**
	 * 菜单名称
	 */
	@Column(name = "name_", length = 50, nullable = false, unique = true)
	@NotNull
	private String name;

	/**
	 * 菜单标题
	 */
	@Column(name = "title_", length = 50)
	private String title;
	/**
	 * 状态 启用true,禁用false
	 */
	@Column(name = "enabled_")
	private boolean enabled = true;

	/**
	 * 菜单表示的地址
	 */
	@Column(name = "url_")
	private String url;

	/**
	 * 菜单排序
	 */
	@Column(name = "order_")
	private Long order;

	/**
	 * 菜单图标
	 */
	@Column(name = "icon_", length = 100)
	private String icon;

	/**
	 * 备注信息
	 */
	@Column(name = "description_", length = 1000)
	private String description;

	@ManyToOne
	@JoinColumn(name = "parent_")
	private Menu parent;

}
