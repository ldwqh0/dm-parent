package com.dm.uap.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

public class LoginLogDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String loginName;

    private String ip;

    private String type;

    private String result;

    private ZonedDateTime time;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginLogDto that = (LoginLogDto) o;
        return Objects.equals(id, that.id) && Objects.equals(loginName, that.loginName) && Objects.equals(ip, that.ip) && Objects.equals(type, that.type) && Objects.equals(result, that.result) && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, loginName, ip, type, result, time);
    }
}
