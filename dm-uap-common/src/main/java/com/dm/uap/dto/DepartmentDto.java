package com.dm.uap.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;

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

	@JsonIgnoreProperties(value = { "parent", "description" })
	private DepartmentDto parent;

//	private List<String> parents;

	/**
	 * 这个属性仅仅用于接受从前台表单传入的departments属性
	 * 
	 * @param parents
	 */
	public void setParents(List<Long> parents) {
		if (CollectionUtils.isNotEmpty(parents)) {
			Long parentId = parents.get(parents.size() - 1);
			this.parent = new DepartmentDto();
			this.parent.setId(parentId);
		}
	}

	public List<Long> getParents() {
		List<Long> results = new ArrayList<Long>();
		addParents(results, this);
		return results;
	}

	private void addParents(List<Long> collector, DepartmentDto dto) {
		DepartmentDto parent = dto.getParent();
		if (!Objects.isNull(parent)) {
			collector.add(0, parent.getId());
			addParents(collector, parent);
		}
	}
}
