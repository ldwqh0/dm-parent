package com.dm.notification.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * 一个通知信息，这个模型仅仅用户数据响应
 */
public class NotificationResponse implements Serializable {
    private static final long serialVersionUID = -6734018394697129662L;
    private final Long id;

    private final Long userid;

    private final String title;

    private final String content;

    private final ZonedDateTime createdTime;

    /**
     * 构造一个消息通知
     *
     * @param id          通知id
     * @param userid      用户id
     * @param title       通知标题
     * @param content     通知内容
     * @param createdTime 通知创建时间
     */
    public NotificationResponse(Long id, Long userid, String title, String content, ZonedDateTime createdTime) {
        this.id = id;
        this.userid = userid;
        this.title = title;
        this.content = content;
        this.createdTime = createdTime;
    }

    public Long getId() {
        return this.id;
    }

    public Long getUserid() {
        return userid;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public ZonedDateTime getCreatedTime() {
        return createdTime;
    }
}
