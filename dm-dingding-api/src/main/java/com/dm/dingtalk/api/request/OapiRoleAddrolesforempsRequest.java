package com.dm.dingtalk.api.request;

import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * TOP DingTalk-API: dingtalk.oapi.role.addrolesforemps request
 * 
 * @author top auto create
 * @since 1.0, 2018.07.25
 */
public class OapiRoleAddrolesforempsRequest implements Serializable {

	private static final long serialVersionUID = -3720159378769411729L;

	private final Collection<Long> roles;

	private final Collection<String> users;

	public String getRoleIds() {
		return StringUtils.join(roles, ",");
	}

	public String getUserIds() {
		return StringUtils.join(users, ",");
	}

	public void addRole(Long roleId) {
		this.roles.add(roleId);
	}

	public void addRoles(Collection<Long> roles) {
		this.roles.addAll(roles);
	}

	public void addUser(String user) {
		this.users.add(user);
	}

	public void addUsers(Collection<String> users) {
		this.users.addAll(users);
	}

	public void removeUser(String user) {
		this.users.remove(user);
	}

	public void removeUser(Predicate<String> user) {
		this.users.removeIf(user);
	}

	public void removeRole(Long role) {
		this.roles.remove(role);
	}

	public void removeRole(Predicate<Long> filter) {
		this.roles.removeIf(filter);
	}

	public OapiRoleAddrolesforempsRequest(Collection<String> users, Collection<Long> roles) {
		super();
		this.roles = roles;
		this.users = users;
	}

	public OapiRoleAddrolesforempsRequest() {
		super();
		this.roles = Collections.synchronizedCollection(new ArrayList<Long>());
		this.users = Collections.synchronizedCollection(new ArrayList<String>());
	}

}