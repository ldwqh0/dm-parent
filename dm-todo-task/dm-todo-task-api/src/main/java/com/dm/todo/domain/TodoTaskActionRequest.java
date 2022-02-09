package com.dm.todo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class TodoTaskActionRequest implements Serializable {

    private static final long serialVersionUID = 6262760628312835932L;

    /**
     * 动作内容
     */
    private final String content;

    /**
     * 事项是否完成
     */
    private final boolean completed;

    @JsonCreator
    public TodoTaskActionRequest(@JsonProperty("completed") boolean completed, @JsonProperty("content") String content) {
        this.content = content;
        this.completed = completed;
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
}
