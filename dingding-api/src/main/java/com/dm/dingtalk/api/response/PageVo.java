package com.dm.dingtalk.api.response;

import java.util.List;

import lombok.Data;

@Data
public class PageVo<T> {
	/**
	 * hasMore
	 */
	private Boolean hasMore;
	/**
	 * list
	 */
	private List<T> list;
	/**
	 * 下次拉取数据的游标
	 */
	private Long nextCursor;
}
