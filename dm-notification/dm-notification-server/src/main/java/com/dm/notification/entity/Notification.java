package com.dm.notification.entity;

import com.dm.data.domain.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table(name = "dm_notification_")
public class Notification extends AbstractEntity {

    /**
     * 通知用户
     */
    @Column(name = "user_id_")
    private Long userid;

    /**
     * 通知标题
     */
    @Column(name = "title_", length = 200, nullable = false)
    public String title;


    /**
     * 通知内容
     */
    @Column(name = "content_", length = 400)
    private String content;

    @Column(name = "created_time_")
    private ZonedDateTime createdTime = ZonedDateTime.now();

    public Notification() {
    }

    public Notification(String title, Long userid, String content) {
        this.title = title;
        this.userid = userid;
        this.content = content;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getCreatedTime() {
        return createdTime;
    }

    private void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }
}
