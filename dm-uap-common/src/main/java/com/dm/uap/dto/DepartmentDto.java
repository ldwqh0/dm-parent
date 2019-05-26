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

	@JsonIgnoreProperties(value = { "parent", "description" })
	private DepartmentDto parent;

	public List<Long> getParents() {
		List<Long> results = new ArrayList<Long>();
		DepartmentDto current = parent;
		while (!Objects.isNull(current)) {
			results.add(0, current.getId());
			current = current.getParent();
		}
		return results;
	}
}
