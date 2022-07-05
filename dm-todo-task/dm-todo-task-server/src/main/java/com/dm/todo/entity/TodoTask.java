package com.dm.todo.entity;

import com.dm.collections.CollectionUtils;
import com.dm.data.domain.AbstractEntity;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.*;

@Entity
@Table(name = "dm_todo_task_", indexes = {
    @Index(name = "IDX_dm_todo_task_user_id_", columnList = "user_id_"),
    @Index(name = "IDX_dm_todo_task_completed_time_", columnList = "completed_time_"),
    @Index(name = "UDX_dm_todo_task_source_biz", columnList = "user_id_,source_name_,biz_id_", unique = true)
})
public class TodoTask extends AbstractEntity {

    /**
     * 用户id
     */
    @Column(name = "user_id_", nullable = false)
    private Long userid;

    /**
     * 待办标题
     */
    @Column(name = "title_", length = 100, nullable = false)
    private String title;

    /**
     * 创建时间
     */
    @Column(name = "created_time_", nullable = false)
    private ZonedDateTime createdTime = ZonedDateTime.now();

    /**
     * 来源名称
     */
    @Column(name = "source_name_", length = 100)
    private String sourceName;

    /**
     * 业务ID
     */
    @Column(name = "biz_id_", length = 100)
    private String bizId;

    @ElementCollection
    @CollectionTable(name = "dm_todo_task_attribute_")
    private final Set<Attribute> attributes = new HashSet<>();

    @Column(name = "completed_time_")
    private ZonedDateTime completedTime;

    /**
     * 待办事项的URL，待办事项的回调URL
     */
    @Column(name = "url", length = 8000, nullable = false)
    private String url;

    /**
     * 待办事项PC端的URL，在PC端，会优先回调到该URL，如果为空的话，会跳转到URL
     */
    @Column(name = "pc_url_", length = 8000)
    private String pcUrl;

    @ElementCollection
    @CollectionTable(name = "dm_todo_task_action_", joinColumns = {@JoinColumn(name = "dm_task_id_")})
    private final List<TodoTaskAction> actions = new ArrayList<>();

    public TodoTask() {
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ZonedDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public void setAttributes(Set<Attribute> attributes) {
        this.attributes.clear();
        CollectionUtils.addAll(this.attributes, attributes);
    }

    public Set<Attribute> getAttributes() {
        return Collections.unmodifiableSet(this.attributes);
    }

    public ZonedDateTime getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(ZonedDateTime completedTime) {
        this.completedTime = completedTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPcUrl() {
        return pcUrl;
    }

    public void setPcUrl(String pcUrl) {
        this.pcUrl = pcUrl;
    }

    private void setActions(List<TodoTaskAction> actions) {
        this.actions.clear();
        CollectionUtils.addAll(this.actions, actions);
    }

    public List<TodoTaskAction> getActions() {
        return Collections.unmodifiableList(this.actions);
    }

    public void addAction(TodoTaskAction action) {
        this.actions.add(action);
    }

    public TodoTaskAction addAction(String content, boolean completed) {
        TodoTaskAction action = new TodoTaskAction(content, completed);
        this.actions.add(action);
        return action;
    }

    public boolean isCompleted() {
        return Objects.nonNull(getCompletedTime());
    }
}
