package com.dm.uap.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

/**
 * 职务
 * 
 * @author LiDong
 *
 */
@Embeddable
@Data
public class Post implements Serializable {
	private static final long serialVersionUID = 7941849108500608959L;

	@Column(name = "name_")
	private String name;

	@ManyToOne
	@JoinColumn(name = "department_id_")
	private Department department;

}
