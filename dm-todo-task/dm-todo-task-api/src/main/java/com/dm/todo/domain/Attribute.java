package com.dm.todo.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.io.Serializable;

/**
 * 待办事项的属性
 */
public class Attribute implements Serializable {
    private static final long serialVersionUID = -4512155978228498305L;

    /**
     * 属性标题
     */
    private final String title;

    /**
     * 属性值
     */
    private final String content;

    @JsonCreator
    public Attribute(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
