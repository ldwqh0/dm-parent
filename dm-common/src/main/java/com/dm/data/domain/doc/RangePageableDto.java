package com.dm.data.domain.doc;

/*
 * 这个仅仅用于smart-doc的类型替换，没有任何实际用途
 */

/**
 * 限定范围的分页请求
 */
public class RangePageableDto extends PageableDto {
    private static final long serialVersionUID = 4804864324327815933L;

    /**
     * 请求的最大范围值,可能是数字或者字符串，根据实际情况而定
     */
    private String max;

    public Object getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }
}
