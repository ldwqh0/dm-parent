package com.dm.dingtalk.api.response;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

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

    public Boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Long getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(Long nextCursor) {
        this.nextCursor = nextCursor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageVo<?> pageVo = (PageVo<?>) o;
        return Objects.equals(hasMore, pageVo.hasMore) && Objects.equals(list, pageVo.list) && Objects.equals(nextCursor, pageVo.nextCursor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hasMore, list, nextCursor);
    }
}
