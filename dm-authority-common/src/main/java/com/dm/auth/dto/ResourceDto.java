package com.dm.auth.dto;

import java.io.Serializable;
import java.util.Set;

import com.dm.security.authentication.UriResource.MatchType;

import lombok.Data;

@Data
public class ResourceDto implements Serializable {
	private static final long serialVersionUID = -847613336378865468L;
	private Long id;
	private String name;
	private String matcher;
	private String description;
	private Set<String> scope;
	private MatchType matchType;
}
