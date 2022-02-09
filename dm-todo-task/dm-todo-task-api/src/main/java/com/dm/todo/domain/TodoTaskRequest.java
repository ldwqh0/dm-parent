package com.dm.todo.domain;

import com.dm.collections.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 一个待办事项
 */
public class TodoTaskRequest implements Serializable {
    private static final long serialVersionUID = 3322580460325704423L;

    /**
     * 用户id
     */
    private final Long userid;

    /**
     * 待办标题
     */
    private final String title;

    /**
     * 来源名称
     */
    private final String sourceName;

    /**
     * 业务ID
     */
    private final String bizId;

    /**
     * 待办事项的属性，用于显示
     */
    private final Set<Attribute> attributes;

    /**
     * 待办事项回调的URL
     */
    private final String url;

    private final String pcUrl;

    /**
     * 默认构造函数，用于构建待办请求
     *
     * @param userid     用户id
     * @param title      待办标题
     * @param sourceName 来源名称
     * @param bizId      业务id
     * @param attributes 待办属性
     * @param url        待办地址
     * @param pcUrl      待办pc端的地址
     */
    @JsonCreator
    public TodoTaskRequest(@JsonProperty("userid") Long userid,
                           @JsonProperty("title") String title,
                           @JsonProperty("sourceName") String sourceName,
                           @JsonProperty("bizId") String bizId,
                           @JsonProperty("attributes") Set<Attribute> attributes,
                           @JsonProperty("url") String url,
                           @JsonProperty("pcUrl") String pcUrl) {
        this.userid = userid;
        this.title = title;
        this.sourceName = sourceName;
        this.bizId = bizId;
        if (CollectionUtils.isNotEmpty(attributes)) {
            this.attributes = new HashSet<>(attributes);
        } else {
            this.attributes = new HashSet<>();
        }
        this.url = url;
        this.pcUrl = pcUrl;
    }

    public Long getUserid() {
        return userid;
    }

    public String getTitle() {
        return title;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getBizId() {
        return bizId;
    }

    public Set<Attribute> getAttributes() {
        return Collections.unmodifiableSet(attributes);
    }

    public String getUrl() {
        return url;
    }

    public String getPcUrl() {
        return pcUrl;
    }

    /**
     * 添加一个新的属性
     *
     * @param title 添加的属性的标题
     * @param value 添加的属性的值
     * @return 添加的属性
     */
    public Attribute addAttribute(String title, String value) {
        Attribute attribute = new Attribute(title, value);
        this.attributes.add(attribute);
        return attribute;
    }
}
