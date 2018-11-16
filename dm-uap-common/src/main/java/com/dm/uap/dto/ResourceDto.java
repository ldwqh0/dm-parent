package com.dm.uap.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ResourceDto implements Serializable {
	private static final long serialVersionUID = -847613336378865468L;
	private Long id;
	private String matcher;
	private String description;
}
