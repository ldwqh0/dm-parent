package com.dm.uap.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.dm.uap.entity.Department.Types;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@Data
@JsonInclude(value = Include.NON_EMPTY)
public class DepartmentDto implements Serializable {

	private static final long serialVersionUID = -4966481409754529111L;

	private Long id;

	private String fullname;

	private String shortname;

	private String description;

	private Types type;

	public DepartmentDto() {
		super();
	}

	public DepartmentDto(Long id) {
		super();
		this.id = id;
	}

	@JsonIgnoreProperties({ "parent", "description", "parents" })
	private DepartmentDto parent;

	/**
	 * 为el-cascade准备的数据结构，在el 2.9中不需要该属性了，后期移除
	 * 
	 * @return
	 */
	@Deprecated
	public List<Long> getParents() {
		List<Long> parents = new ArrayList<Long>();
		DepartmentDto current = parent;
		while (!Objects.isNull(current)) {
			parents.add(0, current.getId());
			current = current.getParent();
		}
		return parents;
	}

}
