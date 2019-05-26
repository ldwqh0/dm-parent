package com.dm.uap.dto;

import java.util.ArrayList;
import java.util.List;

import com.dm.uap.entity.Department.Types;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_EMPTY)
public class DepartmentTreeDto {
	private Long id;
	private Long parentId;
	private String name;
	private String description;
	private Types type;
	private List<DepartmentTreeDto> children = new ArrayList<>();
}
