package com.dm.uap.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
public class ResourceOperationDto implements Serializable {

	private static final long serialVersionUID = 363494892979011485L;

	private ResourceDto resource;

	private boolean readable = false;

	private boolean saveable = false;

	private boolean updateable = false;

	private boolean deleteable = false;

	@JsonIgnoreProperties({ "description", "matcher" })
	public ResourceDto getResource() {
		return resource;
	}

}
