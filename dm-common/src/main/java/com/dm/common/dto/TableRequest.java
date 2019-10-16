package com.dm.common.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class TableRequest implements Serializable {

	private static final long serialVersionUID = -530197666212744386L;
	/**
	 * 请求的戳标记
	 */
	private Long draw;

	/**
	 * 查询最大范围
	 */
	private Long maxId;
}
