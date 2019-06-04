package com.dm.uap.entity.audit;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CreateAudit implements Audit {

	private static final long serialVersionUID = 379407708683930698L;

	@Column(name = "create_user_id_")
	private Long userid;

	@Column(name = "create_user_name_")
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
