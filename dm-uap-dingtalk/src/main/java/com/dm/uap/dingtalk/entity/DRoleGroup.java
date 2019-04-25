package com.dm.uap.dingtalk.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.dm.uap.entity.RoleGroup;

import lombok.Getter;
import lombok.Setter;
import static javax.persistence.CascadeType.*;

@Entity(name = "dd_role_group_")
@Getter
@Setter
public class DRoleGroup {

	@Id
	private Long id;

	@Column(name = "name_")
	private String name;

	@OneToMany(cascade = ALL)
	@JoinColumn(name = "dd_group_id_")
	private Set<DRole> roles;

	@OneToOne(cascade = { DETACH, MERGE, PERSIST, REFRESH })
	@JoinColumn(name = "dm_group_id")
	private RoleGroup group;

	public DRoleGroup(Long id) {
		super();
		this.id = id;
	}

	public DRoleGroup() {
		super();
	}

	void setId(Long id) {
		this.id = id;
	}

}
