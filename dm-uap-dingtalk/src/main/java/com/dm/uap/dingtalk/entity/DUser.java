package com.dm.uap.dingtalk.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "d_user_")
public class DUser implements Serializable {

	private static final long serialVersionUID = -6763998745823230765L;
	
	@Id
	private String unionid;

}
