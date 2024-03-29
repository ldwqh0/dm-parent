package com.dm.springboot.autoconfigure.uap;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 系统默认的初始化用户名和密码
 *
 * @author LiDong
 */
@ConfigurationProperties(prefix = "system.default.su")
public class DefaultUserProperties {

    private String username;

    private String password;

    private String fullName;

    public DefaultUserProperties() {
        super();
        this.username = "admin";
        this.password = "123456";
        this.fullName = "系统管理员";
    }

    /**
     * 获取系统初始化默认用户的用户名
     *
     * @return 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置系统初始化默认用户的用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取系统初始化默认用户的密码
     *
     * @return 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置系统初始化默认用户的密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 设置系统初始化默认用户的全名称
     *
     * @return 全名
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * 获取系统初始化默认用户的全名称
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

}
