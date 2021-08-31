package com.dm.uap.entity;

import com.dm.common.entity.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.ZonedDateTime;

/**
 * 登录日志
 *
 * @author 李东
 */
@Entity
@Table(name = "dm_login_log_", indexes = {@Index(columnList = "login_name_", name = "IDX_dm_login_log_login_name_"),
    @Index(columnList = "time_", name = "IDX_dm_login_log_time_")})
public class LoginLog extends AbstractEntity {

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

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }
}
