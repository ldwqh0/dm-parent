package com.dm.uap.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

import com.dm.common.entity.AbstractEntity;

import javax.persistence.JoinColumn;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "dm_user_")
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends AbstractEntity {

	private static final long serialVersionUID = 9058110335651086815L;

	@Column(name = "username_", unique = true, length = 50)
	@NotNull
	private String username;

	@Column(name = "password_", length = 100)
	@NotNull
	private String password;

	@Column(name = "expired_", nullable = false)
	private boolean accountExpired = false;

	@Column(name = "credentials_expired_", nullable = false)
	private boolean credentialsExpired = false;

	@Column(name = "enabled_", nullable = false)
	private boolean enabled = true;

	@Column(name = "locked_", nullable = false)
	private boolean locked = false;

	@Column(name = "fullname_", length = 200)
	private String fullname;

	@Column(name = "email_", length = 100)
	private String email;

	@Column(name = "mobile_", length = 20)
	private String mobile;

	@Column(name = "describe_", length = 1000)
	private String describe;

	@Column(name = "order_")
	private Long order;

	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinTable(name = "dm_user_role_", joinColumns = {
			@JoinColumn(name = "user_", referencedColumnName = "id_") }, inverseJoinColumns = {
					@JoinColumn(name = "role_", referencedColumnName = "id_") }, indexes = {
							@Index(columnList = "user_", name = "IDX_dm_user_role_user_") })
	private List<Role> roles;

	@Embedded
	private RegionInfo region;

	@Column(name = "scenic_name_", length = 200)
	private String scenicName;
}
