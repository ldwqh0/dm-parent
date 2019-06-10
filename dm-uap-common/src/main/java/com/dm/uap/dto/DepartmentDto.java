package com.dm.uap.dto;

import java.io.Serializable;

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

}
