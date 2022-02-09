package com.dm.todo.entity;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class Attribute implements Serializable {

    private static final long serialVersionUID = 7004547566455691914L;

    @Column(name = "title_", length = 100)
    private String title;

    @Column(name = "content_", length = 400)
    private String content;

    public Attribute() {
    }

    public Attribute(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
