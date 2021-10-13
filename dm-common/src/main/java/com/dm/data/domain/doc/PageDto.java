//package com.dm.data.domain.doc;
//
//import java.io.Serializable;
//import java.util.List;
//
///**
// * 分页响应信息
// */
//public class PageDto<T> implements Serializable {
//    private static final long serialVersionUID = 4638875223699075062L;
//
//    /**
//     * 分页请求参数的原样返回
//     */
//    private PageableDto pageable;
//
//    /**
//     * 响应元素的总量
//     */
//    private Long totalElements;
//
//    /**
//     * 响应的总页数
//     */
//    private Integer totalPages;
//
//    /**
//     * 是否最开始的记录
//     */
//    private Boolean first;
//
//    /**
//     * 是否最后的结果
//     */
//    private Boolean last;
//
//    /**
//     * 是否为空
//     */
//    private Boolean empty;
//
//    /**
//     * 从0开始的页码
//     */
//    private Integer number;
//
//    /**
//     * 当前页面元素的数量
//     */
//    private Integer numberOfElements;
//
//    /**
//     * 每页的数量
//     */
//    private Integer size;
//
//    /**
//     * 数据内容
//     */
//    private List<T> content;
//
//    /**
//     * 排序信息
//     */
//    private Object sort;
//}
