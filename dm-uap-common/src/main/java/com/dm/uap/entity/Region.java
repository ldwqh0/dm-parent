package com.dm.uap.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

/**
 * 区划编码
 * 
 * @author LiDong
 *
 */
@Entity(name = "dm_region_")
@Getter
@Setter
public class Region implements Serializable {

	private static final long serialVersionUID = -6272262504803995206L;

	/**
	 * 编码
	 */
	@Id
	@Column(name = "code_", length = 20)
	private String code;

	/**
	 * 名称
	 */
	@Column(name = "name_", length = 50)
	private String name;

	/**
	 * 上级编码
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_")
	private Region parent;

	@Column(name = "longitude_")
	private Double longitude;

	@Column(name = "latitude_")
	private Double latitude;

}
