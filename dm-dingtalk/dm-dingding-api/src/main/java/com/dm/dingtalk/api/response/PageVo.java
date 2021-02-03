package com.dm.dingtalk.api.response;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class PageVo<T> implements Serializable {
    private static final long serialVersionUID = 8962659967141421912L;
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
