package com.dm.uap.dto;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class GrantedAuthorityDto implements GrantedAuthority {

	private static final long serialVersionUID = 4062924753193768577L;
	private String authority;
	private Long id;

	public GrantedAuthorityDto(String authority, Long id) {
		super();
		this.authority = authority;
		this.id = id;
	}

	public GrantedAuthorityDto(String authority) {
		this(authority, null);
	}

	public GrantedAuthorityDto() {
		this(null);
	}
}