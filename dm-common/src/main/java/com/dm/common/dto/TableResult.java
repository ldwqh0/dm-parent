package com.dm.common.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public interface TableResult<T> extends Serializable {

	public Long getDraw();

	public Long getPage();

	public Long getSize();

	public Long getRecordsTotal();

	public List<T> getData();

	public String getError();

	public boolean isSuccess();

	/**
	 * 使用制定的converter将分页实体数据转换为表格数据
	 * 
	 * @param draw
	 * @param data
	 * @param converter
	 * @return
	 */
	public static <DTO, M> TableResult<DTO> success(Long draw, Page<M> data, Function<M, DTO> converter) {
		TableResultDto<DTO> result = new TableResultDto<DTO>();
		Pageable pageable = data.getPageable();
		result.setDraw(draw);
		result.setRecordsTotal(data.getTotalElements());
		result.setSuccess(Boolean.TRUE);
		result.setPage((long) pageable.getPageNumber());
		result.setSize((long) pageable.getPageSize());
		List<M> contents = data.getContent();
		if (CollectionUtils.isNotEmpty(contents)) {
			result.setData(contents.stream().map(converter).collect(Collectors.toList()));
		} else {
			result.setData(Collections.emptyList());
		}
		return result;
	}

	public static <DTO> TableResult<DTO> success(Long draw, Page<DTO> data) {
		return success(draw, data, d -> d);
	}

	/**
	 * 返回一个表格错误信息<br />
	 * 
	 * @param err
	 * @return
	 */
	public static <DTO, M> TableResult<DTO> failure(Long draw, Pageable pageable, String err) {
		TableResultDto<DTO> result = new TableResultDto<>();
		result.setDraw(draw);
		result.setError(err);
		result.setPage((long) pageable.getPageNumber());
		result.setSize((long) pageable.getPageSize());
		result.setSuccess(false);
		return result;
	}
}
