package com.dm.uap.dto;

import java.io.Serializable;

import lombok.Data;

@Data
/**
 * 表示用户的职务信息
 * 
 * @author LiDong
 *
 */
public class UserPostDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1344418453725050901L;

	private DepartmentDto department;

	private String post;

	public UserPostDto(DepartmentDto department, String post) {
		super();
		this.department = department;
		this.post = post;
	}

	public UserPostDto() {
		super();
	}

}
