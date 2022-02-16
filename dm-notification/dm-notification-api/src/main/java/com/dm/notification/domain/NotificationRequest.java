package com.dm.notification.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * 一个通知请求
 */
public class NotificationRequest implements Serializable {
    private static final long serialVersionUID = -6397347690838364837L;

    /**
     * 通知用户
     */
    private final Long userid;

    /**
     * 通知标题
     */
    private final String title;

    /**
     * 通知内容
     */
    private final String content;

    /**
     * 构建一个通知请求
     *
     * @param userid  通知对象
     * @param title   通知标题
     * @param content 通知内容
     */
    @JsonCreator
    public NotificationRequest(@JsonProperty("userid") Long userid,
                               @JsonProperty("title") String title,
                               @JsonProperty("content") String content) {
        this.userid = userid;
        this.title = title;
        this.content = content;
    }

    /**
     * 通知对象
     */
    public Long getUserid() {
        return userid;
    }

    /**
     * 通知标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 通知内容
     */
    public String getContent() {
        return content;
    }

}
