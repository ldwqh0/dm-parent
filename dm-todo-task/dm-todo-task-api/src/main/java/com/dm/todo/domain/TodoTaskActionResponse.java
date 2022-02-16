package com.dm.todo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class TodoTaskActionResponse implements Serializable {

    private static final long serialVersionUID = 6262760628312835932L;

    /**
     * 待办事项的id
     */
    private final Long taskId;

    /**
     * 动作内容
     */
    private final String content;

    /**
     * 事项是否完成
     */
    private final boolean completed;

    /**
     * 待办事项的创建时间
     */
    private final ZonedDateTime createdTime;

    @JsonCreator
    public TodoTaskActionResponse(@JsonProperty("taskId") Long taskId,
                                  @JsonProperty("content") String content,
                                  @JsonProperty(value = "completed", defaultValue = "false") boolean completed,
                                  @JsonProperty("createdTime") ZonedDateTime createdTime) {
        this.taskId = taskId;
        this.content = content;
        this.completed = completed;
        this.createdTime = createdTime;
    }

    /**
     * 待办事项的id
     */
    public Long getTaskId() {
        return taskId;
    }

    /**
     * 待办动作的内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 该动作是否某个待办事项的完成动作，是否将待办事项标记为完成
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * 动作的产生时间
     */
    public ZonedDateTime getCreatedTime() {
        return createdTime;
    }
}
