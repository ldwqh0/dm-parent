package com.dm.springboot.autoconfigure.uap;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 系统默认的初始化用户名和密码
 * 
 * @author LiDong
 *
 */
@ConfigurationProperties(prefix = "system.default.su")
public class DefaultUserProperties {

	private String username;

	private String password;

	private String fullname;

	public DefaultUserProperties() {
		super();
		this.username = "admin";
		this.password = "123456";
		this.fullname = "系统管理员";
	}

	/**
	 * 获取系统初始化默认用户的用户名
	 * 
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 设置系统初始化默认用户的用户名
	 * 
	 * @return
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 获取系统初始化默认用户的密码
	 * 
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 设置系统初始化默认用户的密码
	 * 
	 * @return
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 设置系统初始化默认用户的全名称
	 * 
	 * @return
	 */
	public String getFullname() {
		return fullname;
	}

	/**
	 * 获取系统初始化默认用户的全名称
	 * 
	 * @return
	 */
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

}
