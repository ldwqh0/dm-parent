package com.dm.common.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.dm.common.entity.Audit;

@Embeddable
public class CreateAudit implements Audit {

	private static final long serialVersionUID = 379407708683930698L;

	/**
	 * 创建人相关信息不能被修改
	 */
	@Column(name = "created_user_id_", updatable = false)
	private Long userid;
	
	/**
	 * 创建人相关信息不能被修改
	 */
	@Column(name = "created_user_name_", updatable = false)
	private String username;

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public Long getUserid() {
		return userid;
	}

	public CreateAudit(Long createUserId, String createUserName) {
		super();
		this.userid = createUserId;
		this.username = createUserName;
	}

	public CreateAudit() {
		super();
	}

	public CreateAudit(Audit audit) {
		this.userid = audit.getUserid();
		this.username = audit.getUsername();
	}

}
