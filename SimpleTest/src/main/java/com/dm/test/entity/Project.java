package com.dm.test.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.dm.common.entity.AbstractEntity;

@Entity(name = "project_")
public class Project extends AbstractEntity {

	private static final long serialVersionUID = 472271335434836600L;

	@Column(name = "name_")
	private String name;

}
