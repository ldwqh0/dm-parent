package com.dm.auth.dto;

import java.io.Serializable;

import com.dm.auth.entity.Menu.MenuType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * 菜单结构
 * 
 * @author LiDong
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
public class MenuDto implements Serializable {
	private static final long serialVersionUID = 7184771144233410172L;
	private Long id;
	private String name;
	private String title;
	private Boolean enabled;
	private String url;
	private String icon;
	private String description;
	private MenuType type;
	private MenuDto parent;
}