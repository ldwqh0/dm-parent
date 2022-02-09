package com.dm.todo.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Embeddable
public class TodoTaskAction implements Serializable {

    private static final long serialVersionUID = 698212146287794325L;

    @Column(name = "created_time_")
    private ZonedDateTime createdTime = ZonedDateTime.now();

    @Column(name = "content_", length = 4000)
    private String content = "";

    @Column(name = "completed_")
    private boolean completed = false;

    public TodoTaskAction() {
    }

    public TodoTaskAction(String content, boolean completed) {
        this.content = content;
        this.completed = completed;
        this.createdTime = ZonedDateTime.now();
    }

    public ZonedDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
