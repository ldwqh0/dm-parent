package com.dm.auth.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.oauth2.provider.approval.Approval.ApprovalStatus;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "dm_user_approval_")
@Getter
@Setter
@IdClass(UserApprovalPK.class)
@EntityListeners(AuditingEntityListener.class)
public class UserApproval {
	@Id
	@Column(name = "user_id_", length = 50)
	private String userId;

	@Id
	@Column(name = "client_id_", length = 50)
	private String clientId;

	@Id
	@Column(name = "scope_", length = 100)
	private String scope;

	@Column(name = "status_")
	@Enumerated(EnumType.STRING)
	private ApprovalStatus status;

	@Column(name = "expires_at_")
	private Date expiresAt;

	@Column(name = "last_update_at_")
	@LastModifiedDate
	private Date lastUpdatedAt;

	protected void setLastUpdatedAt(Date date) {
		this.lastUpdatedAt = date;
	}

}
