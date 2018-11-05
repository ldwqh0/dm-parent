package com.dm.common.entity;

import java.time.ZonedDateTime;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 一个简单的，仅仅包含时间的审计实体类
 * 
 * @author LiDong
 *
 * @param <U>
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractSimpleAuditEntity extends AbstractEntity {

	private static final long serialVersionUID = -8609700737505668067L;

	@CreatedDate
	@Column(name = "create_date_")
	private ZonedDateTime createDate;

	@Column(name = "last_modified_date_")
	@LastModifiedDate
	private ZonedDateTime lastModifiedDate;

	public Optional<ZonedDateTime> getCreatedDate() {
		return Optional.ofNullable(createDate);
	}

	public void setCreatedDate(ZonedDateTime creationDate) {
		this.createDate = creationDate;
	}

	public Optional<ZonedDateTime> getLastModifiedDate() {
		return Optional.ofNullable(lastModifiedDate);
	}

	public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

}
