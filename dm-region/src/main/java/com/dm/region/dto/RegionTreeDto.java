package com.dm.region.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

@Data
@JsonInclude(content = Include.NON_EMPTY)
public class RegionTreeDto implements Serializable {
	private static final long serialVersionUID = -5723991352673491174L;
	@JsonProperty(access = Access.READ_ONLY)
	@JsonInclude(Include.NON_EMPTY)
	private final List<RegionTreeDto> children;

	/**
	 * 编码
	 */
	private String code;

	/**
	 * 名称
	 */
	private String name;

	private Double longitude;

	private Double latitude;

	private String parentCode;

	public RegionTreeDto() {
		super();
		children = Collections.synchronizedList(new ArrayList<>());
	}

	public void addChildren(RegionTreeDto child) {
		this.children.add(child);
	}

	public void cleanChildren() {
		this.children.clear();
	}

	public void removeChidren(RegionTreeDto child) {
		this.children.remove(child);
	}
}
