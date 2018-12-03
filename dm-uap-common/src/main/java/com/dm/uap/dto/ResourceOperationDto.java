package com.dm.uap.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
public class ResourceOperationDto implements Serializable {

	private static final long serialVersionUID = 363494892979011485L;

	private ResourceDto resource;

	private Boolean readable = false;

	private Boolean saveable = false;

	private Boolean updateable = false;

	private Boolean deleteable = false;

	@JsonIgnoreProperties({ "description" })
	public ResourceDto getResource() {
		return resource;
	}

}
