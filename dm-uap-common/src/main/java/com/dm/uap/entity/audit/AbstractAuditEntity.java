package com.dm.uap.entity.audit;

import java.time.ZonedDateTime;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dm.common.entity.AbstractEntity;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditEntity extends AbstractEntity implements Auditable<Audit, Long, ZonedDateTime> {

	private static final long serialVersionUID = -3422581450045291219L;

	private CreateAudit createBy;

	private ModifyAudit lastModifiedBy;

	@CreatedDate
	@Column(name = "create_date_")
	private ZonedDateTime createDate;

	@LastModifiedDate
	@Column(name = "last_modified_date_")
	private ZonedDateTime lastModifiedDate;

	@Override

	public Optional<Audit> getCreatedBy() {
		return Optional.ofNullable(this.createBy);
	}

	@Override
	@CreatedBy
	public void setCreatedBy(Audit createdBy) {
		this.createBy = new CreateAudit(createdBy);
	}

	@Override
	public Optional<ZonedDateTime> getCreatedDate() {
		return Optional.ofNullable(this.createDate);
	}

	@Override
	public void setCreatedDate(ZonedDateTime creationDate) {
		this.createDate = creationDate;

	}

	@Override
	public Optional<Audit> getLastModifiedBy() {
		return Optional.ofNullable(this.lastModifiedBy);
	}

	@Override
	@LastModifiedBy
	public void setLastModifiedBy(Audit lastModifiedBy) {
		this.lastModifiedBy = new ModifyAudit(lastModifiedBy);
	}

	@Override
	public Optional<ZonedDateTime> getLastModifiedDate() {
		return Optional.ofNullable(this.lastModifiedDate);
	}

	@Override
	public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
}
