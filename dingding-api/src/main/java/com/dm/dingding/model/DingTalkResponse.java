package com.dm.dingding.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.*;

@Data
public class DingTalkResponse {
	@JsonProperty(access = WRITE_ONLY)
	private String errmsg;

	@JsonProperty(access = WRITE_ONLY)
	private int errcode;
}
