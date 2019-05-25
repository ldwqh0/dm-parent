package com.dm.uap.dto;

import java.util.ArrayList;
import java.util.List;

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
	private List<DepartmentTreeDto> children = new ArrayList<>();
}
