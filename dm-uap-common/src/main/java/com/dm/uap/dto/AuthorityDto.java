package com.dm.uap.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * 用户授权信息
 * 
 * @author LiDong
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
public class AuthorityDto implements Serializable {
	private static final long serialVersionUID = 4813613447760388284L;
	private Long roleId;
	private List<MenuDto> authorityMenus;
}
