package com.dm.zuul.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.dm.common.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "dm_role_")
public class Role extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -371587731160110775L;

	@Column(name = "name_")
	private String name;

}
