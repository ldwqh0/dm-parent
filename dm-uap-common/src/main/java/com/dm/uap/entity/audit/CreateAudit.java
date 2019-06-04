package com.dm.uap.entity.audit;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CreateAudit implements Audit {

	private static final long serialVersionUID = 379407708683930698L;

	@Column(name = "create_user_id_")
	private Long createUserId;

	@Column(name = "create_user_name_")
	private String createUserName;

	@Override
	public String getUsername() {
		return createUserName;
	}

	@Override
	public Long getUserid() {
		return createUserId;
	}

	public CreateAudit(Long createUserId, String createUserName) {
		super();
		this.createUserId = createUserId;
		this.createUserName = createUserName;
	}

	public CreateAudit() {
		super();
	}

	public CreateAudit(Audit audit) {
		this.createUserId = audit.getUserid();
		this.createUserName = audit.getUsername();
	}

}
