package com.dm.uap.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

import com.dm.common.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "dm_role_")
public class Role extends AbstractEntity {

	public enum Status {
		/**
		 * 标识角色已启用
		 */
		ENABLED,
		/**
		 * 标识角色被禁用
		 */
		DISABLED
	}

	private static final long serialVersionUID = -7562410173176807166L;

	@Column(name = "name_", length = 100, unique = true, nullable = false)
	@NotNull
	private String name;

	@Column(name = "state_")
	@Enumerated(EnumType.STRING)
	private Status state;

	@Column(name = "describe_", length = 1000)
	private String describe;

	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinTable(name = "dm_user_role_", joinColumns = {
			@JoinColumn(name = "role_", referencedColumnName = "id_") }, inverseJoinColumns = {
					@JoinColumn(name = "user_", referencedColumnName = "id_") }, indexes = {
							@Index(columnList = "role_", name = "IDX_dm_user_role_role_") })
	private List<User> users;

}
