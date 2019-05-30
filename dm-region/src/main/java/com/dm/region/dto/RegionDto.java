package com.dm.region.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_EMPTY)
public class RegionDto implements Serializable {

	private static final long serialVersionUID = 1L;

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

	/**
	 * 上级编码
	 */
	@JsonIgnoreProperties("parent")
	private RegionDto parent;

	public RegionDto() {

	}

	public RegionDto(String code, String name, RegionDto parentCode) {
		this.code = code;
		this.name = name;
		this.parent = parentCode;
	}

	public RegionDto(String name) {
		this.name = name;
	}

	public RegionDto(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public List<String> getParents() {
		List<String> results = new ArrayList<String>();
		RegionDto parent = this.parent;
		while (!Objects.isNull(parent)) {
			results.add(0, parent.getCode());
			parent = parent.getParent();
		}
		return results;
	}

}
