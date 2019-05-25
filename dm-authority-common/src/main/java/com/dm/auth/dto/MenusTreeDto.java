package com.dm.auth.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.dm.auth.entity.Menu.MenuType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * 树形的菜单结构
 * 
 * @author LiDong
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
public class MenusTreeDto implements Serializable {
	private static final long serialVersionUID = -7188465645966903302L;
	private Long id;
	private String name;
	private String title;
	private String icon;
	private String href;
	private String description;
	private MenuType type;
	private List<MenusTreeDto> submenus;

	public MenusTreeDto() {
		submenus = new ArrayList<MenusTreeDto>();
	}
}
