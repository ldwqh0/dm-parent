package com.dm.region.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
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
	@JsonIgnoreProperties("parentCode")
	private RegionDto parentCode;

	public RegionDto() {

	}

	public RegionDto(String code, String name, RegionDto parentCode) {
		this.code = code;
		this.name = name;
		this.parentCode = parentCode;
	}

	public RegionDto(String name) {
		this.name = name;
	}

	public RegionDto(String code, String name) {
		this.code = code;
		this.name = name;
	}

}
