package com.dm.common.entity;

import java.time.ZonedDateTime;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dm.uap.entity.User;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditEntity extends AbstractEntity implements Auditable<User, Long, ZonedDateTime> {

	private static final long serialVersionUID = -2512504202930991733L;

	@JoinColumn(name = "create_by_")
	@CreatedBy
	@ManyToOne
	private User createBy;

	@Column(name = "create_date_")
	private ZonedDateTime createDate;

	@JoinColumn(name = "last_modified_by_")
	@LastModifiedBy
	@ManyToOne
	private User lastModifiedBy;

	@Column(name = "last_modified_date_")
	private ZonedDateTime lastModifiedDate;

	@Override
	public Optional<User> getCreatedBy() {
		return Optional.ofNullable(createBy);
	}

	@Override
	public void setCreatedBy(User createdBy) {
		this.createBy = createdBy;
	}

	@Override
	public Optional<ZonedDateTime> getCreatedDate() {
		return Optional.ofNullable(createDate);
	}

	@Override
	public void setCreatedDate(ZonedDateTime creationDate) {
		this.createDate = creationDate;
	}

	@Override
	public Optional<User> getLastModifiedBy() {
		return Optional.ofNullable(lastModifiedBy);
	}

	@Override
	public void setLastModifiedBy(User lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Override
	public Optional<ZonedDateTime> getLastModifiedDate() {
		return Optional.ofNullable(lastModifiedDate);
	}

	@Override
	public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;

	}

}
