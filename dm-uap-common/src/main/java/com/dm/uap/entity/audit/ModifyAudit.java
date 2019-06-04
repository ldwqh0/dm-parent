package com.dm.uap.entity.audit;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ModifyAudit implements Audit {

	private static final long serialVersionUID = 5546618897219690297L;

	@Column(name = "last_modify_user_id_")
	private Long lastModifiedUserId;

	@Column(name = "last_modify_user_name_")
	private String lastModifiedUserName;

	public ModifyAudit() {
		super();
	}

	public ModifyAudit(Long lastModifiedUserId, String lastModifiedUserName) {
		super();
		this.lastModifiedUserId = lastModifiedUserId;
		this.lastModifiedUserName = lastModifiedUserName;
	}

	public ModifyAudit(Audit audit) {
		super();
		this.lastModifiedUserId = audit.getUserid();
		this.lastModifiedUserName = audit.getUsername();
	}

	@Override
	public Long getUserid() {
		return this.lastModifiedUserId;
	}

	@Override
	public String getUsername() {
		return this.lastModifiedUserName;
	}

}
