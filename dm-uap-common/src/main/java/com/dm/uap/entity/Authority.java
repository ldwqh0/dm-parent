package com.dm.uap.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

/**
 * 授权信息
 * 
 * @author LiDong
 *
 */
@Entity(name = "dm_authority_")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
public class Authority implements Serializable {

	private static final long serialVersionUID = 1819180600973309677L;

	@Id
	@Column(name = "role_id_")
	private Long id;

	@OneToOne
	@MapsId
	@JoinColumn(name = "role_id_")
	private Role role;

	@Column(name = "describe_")
	private String describe;

	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinTable(name = "dm_authority_menu_", joinColumns = {
			@JoinColumn(name = "authority_id_", referencedColumnName = "role_id_") }, inverseJoinColumns = {
					@JoinColumn(name = "menu_id_", referencedColumnName = "id_") }, indexes = {
							@Index(columnList = "authority_id_", name = "IDX_dm_authority_menu_authority_id_") })
	private List<Menu> menus;

	protected void setId(Long id) {
		this.id = id;
	}

}
