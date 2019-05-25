package com.dm.uap.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UpdatePasswordDto {
	@NotBlank
	private String oldPassword;

	@NotBlank
	private String password;

	@NotBlank
	private String repassword;
}
