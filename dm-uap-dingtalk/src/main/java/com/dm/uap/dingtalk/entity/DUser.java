package com.dm.uap.dingtalk.entity;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyJoinColumn;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "dd_user_")
@Getter
@Setter
public class DUser implements Serializable {

	private static final long serialVersionUID = -6763998745823230765L;

	@Column(name = "unionid_")
	private String unionid;

	@Id
	@Column(name = "userid_")
	private String userid;

	@Column(name = "name_")
	private String name;

	@Column(name = "tel_")
	private String tel;

	@Column(name = "work_place_")
	private String workPlace;

	@Column(name = "remark_")
	private String remark;

	@Column(name = "mobile_")
	private String mobile;

	@Column(name = "email_")
	private String email;

	@Column(name = "org_email_")
	private String orgEmail;

	@Column(name = "active_")
	private Boolean active;

	@Column(name = "order_")
	@JoinTable(name = "dd_department_user_order_", joinColumns = {
			@JoinColumn(name = "dd_user_id_")
	})
	@ElementCollection
	@MapKeyJoinColumn(name = "dd_department_id_")
	private Map<DDepartment, Long> orderInDepts;

	@Column(name = "is_admin_")
	private Boolean admin;

	@Column(name = "is_boss_")
	private Boolean boss;

	@Column(name = "is_leader_")
	@ElementCollection
	@JoinTable(name = "dd_department_user_leader_", joinColumns = {
			@JoinColumn(name = "dd_user_id_")
	})
	@MapKeyJoinColumn(name = "dd_department_id_")
	private Map<DDepartment, Boolean> leaderInDepts;

	@Column(name = "hide_")
	private Boolean hide;

	@ManyToMany
	@JoinTable(name = "dd_department_dd_user_", joinColumns = {
			@JoinColumn(name = "dd_user_id_")
	}, inverseJoinColumns = {
			@JoinColumn(name = "dd_department_id_")
	})
	private List<DDepartment> departments;

	@Column(name = "position_")
	private String position;

	@Column(name = "avatar_")
	private String avatar;

	@Column(name = "hired_date_")
	private ZonedDateTime hiredDate;

	@Column(name = "jobnumber_")
	private String jobnumber;

	@Column(name = "senior_")
	private Boolean senior;

	@Column(name = "state_code_")
	private String stateCode;

	public DUser() {
		super();
	}

	public DUser(String userid) {
		super();
		this.userid = userid;
	}

}
