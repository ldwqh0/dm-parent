package com.dm.uap.entity;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.dm.common.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 登录日志
 * 
 * @author 李东
 *
 */
@Entity(name = "dm_login_log_")
@Getter
@Setter
@Table(indexes = { @Index(columnList = "login_name_", name = "idx_login_name_"),
        @Index(columnList = "time_", name = "idx_time_") })
public class LoginLog extends AbstractEntity {

    /**
     * 
     */
    private static final long serialVersionUID = -7542534541704477309L;

    @Column(name = "login_name_", length = 250)
    private String loginName;

    @Column(name = "ip_", length = 100)
    private String ip;

    @Column(name = "type_", length = 20)
    private String type;

    @Column(name = "result_", length = 50)
    private String result;

    @Column(name = "time_")
    private ZonedDateTime time;

}
