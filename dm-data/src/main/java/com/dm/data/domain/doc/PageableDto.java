package com.dm.data.domain.doc;

import org.apache.commons.lang3.NotImplementedException;

import java.io.Serializable;
// 这个仅仅用于smart-doc的类型替换，没有任何实际用途

/**
 * 分页请求
 */
public class PageableDto implements Serializable {
    private static final long serialVersionUID = 4804864324327815933L;

    /**
     * 页码，从0开始
     */
    private Integer page;

    /**
     * 每页长度
     */
    private Integer size;

    /**
     * 排序信息，形如 sort=id,asc&sort=name,desc
     */
    private String[] sort;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String[] getSort() {
        throw new NotImplementedException();
    }

    public void setSort(String[] sort) {
        throw new NotImplementedException();
    }
}
