package com.dm.uap.dto;

import lombok.Data;

@Data
public class RegionDto {
	private String code;
	private String name;
	private RegionDto parent;
}
