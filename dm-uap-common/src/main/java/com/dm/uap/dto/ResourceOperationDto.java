package com.dm.uap.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ResourceOperationDto implements Serializable {

	private static final long serialVersionUID = 363494892979011485L;

	private ResourceDto resource;

	private boolean readable = true;

	private boolean saveable = false;

	private boolean updateable = false;

	private boolean deleteable = false;

}
