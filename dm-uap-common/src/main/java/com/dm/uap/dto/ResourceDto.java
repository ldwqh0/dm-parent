package com.dm.uap.dto;

import java.io.Serializable;

import com.dm.security.access.RequestAuthorityAttribute.MatchType;

import lombok.Data;

@Data
public class ResourceDto implements Serializable {
	private static final long serialVersionUID = -847613336378865468L;
	private Long id;
	private String name;
	private String matcher;
	private String description;
	private MatchType matchType;
}
