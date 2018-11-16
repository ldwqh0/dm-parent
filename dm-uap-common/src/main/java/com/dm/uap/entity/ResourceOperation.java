package com.dm.uap.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class ResourceOperation implements Serializable {

	private static final long serialVersionUID = -5174742893339428853L;

	@ManyToOne
	private Resource resource;

	@Column(name = "read_able_")
	private boolean readable = true;

	@Column(name = "save_able_")
	private boolean saveable = false;

	@Column(name = "update_able_")
	private boolean updateable = false;

	@Column(name = "delete_able_")
	private boolean deleteable = false;
	
	

}
