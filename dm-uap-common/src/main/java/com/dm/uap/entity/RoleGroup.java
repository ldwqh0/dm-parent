package com.dm.uap.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.dm.common.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "dm_role_group_")
@Getter
@Setter
public class RoleGroup extends AbstractEntity {
	private static final long serialVersionUID = 3288697536654269610L;

	@Column(name = "name_", nullable = false, unique = true)
	private String name;

	@Column(name = "description_", length = 500)
	private String description;

	@OneToMany(mappedBy = "group")
	private Set<Role> roles;

}
