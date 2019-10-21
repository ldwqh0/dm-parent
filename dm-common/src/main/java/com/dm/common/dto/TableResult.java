package com.dm.common.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 用于表格请求的响应<br>
 * 一个正确的完整的表格响应需要包括：<br/>
 * success: <b>必须</b>，查询是否成功的标记<br/>
 * draw: <b>非必须</b>，请求戳标记.这个标记由客户端提供，服务器原样返回该标记，用以客户端验证某次的请求是否和某次响应相匹配<br>
 * recordsTotal: <b>必须</b>，查询后未分页的数据总量<br/>
 * data: <b>必须</b>，在当前分页模式下，返回的数据<br/>
 * maxId: <b>非必须</b> 在上拉刷新的表格模式中，需要前后端确定maxId,用以确认每次刷新和翻页的数据范围
 * 如果表格响应错误，则应该在message中包含错误的详情
 * 
 * @author LiDong
 * 
 * 
 *
 * @param <T> 列表的数据类型
 */
@JsonInclude(Include.NON_NULL)
public interface TableResult<T> extends Serializable {

    public Long getDraw();

    public Long getPage();

    public Long getSize();

    public Long getRecordsTotal();

    public List<T> getData();

    public String getError();

    public Object getMaxId();

    public default String getMessage() {
        return getError();
    }

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

    public static <DTO, M> TableResult<DTO> success(TableRequest request, RangePage<M> data,
            Function<M, DTO> converter) {
        TableResultDto<DTO> result = new TableResultDto<DTO>();
        result.setDraw(request.getDraw());
        // 如果请求中有maxId,将maxId原样返回
        if (!Objects.isNull(request.getMaxId())) {
            result.setMaxId(request.getMaxId());
        } else {
            // 否则将查询出来的maxId返回
            result.setMaxId(data.getMax());
        }
        Pageable pageable = data.getPageable();
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
}
