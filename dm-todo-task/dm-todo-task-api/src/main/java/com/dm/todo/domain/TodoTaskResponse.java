package com.dm.todo.domain;

import com.dm.collections.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * 待办事项模型
 */
public class TodoTaskResponse implements Serializable {

    private static final long serialVersionUID = 7478338526925516524L;

    /**
     * 待办ID
     */
    @JsonProperty(access = READ_ONLY)
    private final Long id;

    /**
     * 用户id
     */
    private final Long userid;

    /**
     * 待办标题
     */
    private final String title;

    /**
     * 创建时间
     */
    @JsonProperty(access = READ_ONLY)
    private final ZonedDateTime createdTime;

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
    private final Set<Attribute> attributes = new HashSet<>();

    @JsonProperty(access = READ_ONLY)
    private final ZonedDateTime completedTime;

    /**
     * 待办事项回调的URL
     */
    private final String url;

    private final String pcUrl;

    /**
     * 默认构造函数
     *
     * @param id            待办事项的id
     * @param userid        用户id
     * @param title         待办标题
     * @param createdTime   创建时间
     * @param sourceName    来源名称
     * @param bizId         业务id
     * @param attributes    待办属性
     * @param url           待办地址
     * @param pcUrl         待办pc端的地址
     * @param completedTime 完成时间
     */
    @JsonCreator
    public TodoTaskResponse(@JsonProperty("id") Long id,
                            @JsonProperty("userid") Long userid,
                            @JsonProperty("title") String title,
                            @JsonProperty("createdTime") ZonedDateTime createdTime,
                            @JsonProperty("sourceName") String sourceName,
                            @JsonProperty("bizId") String bizId,
                            @JsonProperty("attributes") Set<Attribute> attributes,
                            @JsonProperty("url") String url,
                            @JsonProperty("pcUrl") String pcUrl,
                            @JsonProperty("completedTime") ZonedDateTime completedTime) {
        this.id = id;
        this.userid = userid;
        this.title = title;
        this.createdTime = createdTime;
        this.sourceName = sourceName;
        this.bizId = bizId;
        if (CollectionUtils.isNotEmpty(attributes)) {
            this.attributes.addAll(attributes);
        }
        this.completedTime = completedTime;
        this.url = url;
        this.pcUrl = pcUrl;
    }

    public Set<Attribute> getAttributes() {
        return Collections.unmodifiableSet(this.attributes);
    }

    @JsonGetter("completed")
    public boolean isCompleted() {
        return Objects.nonNull(this.completedTime);
    }

    public Long getId() {
        return id;
    }

    public Long getUserid() {
        return userid;
    }

    public String getTitle() {
        return title;
    }

    public ZonedDateTime getCreatedTime() {
        return createdTime;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getBizId() {
        return bizId;
    }

    public ZonedDateTime getCompletedTime() {
        return completedTime;
    }

    public String getUrl() {
        return url;
    }

    public String getPcUrl() {
        return pcUrl;
    }
}
