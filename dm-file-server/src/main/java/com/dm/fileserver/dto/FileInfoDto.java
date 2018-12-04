package com.dm.fileserver.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_EMPTY)
public class FileInfoDto implements Serializable {

	private static final long serialVersionUID = -6472426570089325611L;

	private UUID id;

	private String filename;

	@JsonIgnore
	private String path;

	private Long size;

	private String createUser;

	private ZonedDateTime createTime;
}
