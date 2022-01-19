package com.dm.uap.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

public class LoginLogDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long id;

    private final String loginName;

    private final String ip;

    private final String type;

    private final String result;

    private final ZonedDateTime time;

    public LoginLogDto(Long id, String loginName, String ip, String type, String result, ZonedDateTime time) {
        this.id = id;
        this.loginName = loginName;
        this.ip = ip;
        this.type = type;
        this.result = result;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getIp() {
        return ip;
    }

    public String getType() {
        return type;
    }

    public String getResult() {
        return result;
    }

    public ZonedDateTime getTime() {
        return time;
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
