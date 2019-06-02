package com.dm.uap.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

	/**
	 * 为el-cacade 准备的数据结构,<br >
	 * 
	 * 在element-2.9中，已经不需要如此处理了
	 * 
	 * @return
	 */
	@Deprecated
	public List<Long> getDepartments() {
		List<Long> departments = new ArrayList<Long>();
		if (!Objects.isNull(department)) {
			departments.addAll(department.getParents());
			departments.add(department.getId());
		}
		return departments;
	}

}
