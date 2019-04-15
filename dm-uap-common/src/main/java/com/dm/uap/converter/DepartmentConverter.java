package com.dm.uap.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.dm.common.converter.AbstractConverter;
import com.dm.uap.dto.DepartmentDto;
import com.dm.uap.dto.DepartmentTreeDto;
import com.dm.uap.entity.Department;

@Component
public class DepartmentConverter extends AbstractConverter<Department, DepartmentDto> {

	@Override
	protected DepartmentDto toDtoActual(Department model) {
		DepartmentDto result = new DepartmentDto();
		result.setId(model.getId());
		result.setName(model.getName());
		result.setDescription(model.getDescription());
		Department parent = model.getParent();
		if (!Objects.isNull(parent)) {
			result.setParent(toDtoActual(model.getParent()));
		}
		return result;
	}

	@Override
	public void copyProperties(Department model, DepartmentDto dto) {
		model.setName(dto.getName());
		model.setDescription(dto.getDescription());
	}

	/**
	 * 转换部门的树形结构
	 * 
	 * @param departments
	 * @return
	 */
	public List<DepartmentTreeDto> toTree(List<Department> departments) {
		Map<Long, DepartmentTreeDto> dMap = new HashMap<>();
		departments.forEach(dep -> {
			dMap.put(dep.getId(), this.toTreeDto(dep));
		});
		List<DepartmentTreeDto> results = new ArrayList<>(dMap.values());
		results.forEach(dep -> {
			if (!Objects.isNull(dep.getParentId())) {
				DepartmentTreeDto parent = dMap.get(dep.getParentId());
				DepartmentTreeDto current = dMap.get(dep.getId());
				parent.getChildren().add(current);
			}
		});
		return results.stream().filter(dep -> Objects.isNull(dep.getId())).collect(Collectors.toList());
	}

	private DepartmentTreeDto toTreeDto(Department department) {
		DepartmentTreeDto tree = new DepartmentTreeDto();
		tree.setId(department.getId());
		tree.setName(department.getName());
		tree.setDescription(department.getDescription());
		Department parent = department.getParent();
		if (!Objects.isNull(parent)) {
			tree.setParentId(parent.getId());
		}
		return tree;
	}
}
