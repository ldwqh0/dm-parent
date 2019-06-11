package com.dm.fileserver.entity;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dm.common.entity.Audit;
import com.dm.common.entity.CreateAudit;
import com.dm.common.entity.ModifyAudit;

@Entity(name = "dm_file_")
@EntityListeners(AuditingEntityListener.class)
public class FileInfo implements Auditable<Audit, UUID, ZonedDateTime>, Serializable {
	private static final long serialVersionUID = -914974010332311193L;

	@Id
	@GeneratedValue
	@Type(type = "uuid-char")
	@Column(name = "id_", length = 36)
	private @Nullable UUID id;

	private CreateAudit createBy;

	@Column(name = "created_date_")
	@CreatedDate
	private ZonedDateTime createdDate;

	private ModifyAudit lastModifiedBy;

	@Column(name = "last_modified_date_")
	@LastModifiedDate
	private ZonedDateTime lastModifiedDate;

	/**
	 * 文件名
	 */
	@NotNull
	@Column(name = "file_name_", length = 255, nullable = false)
	private String filename;

	/**
	 * 文件存储路径
	 */
	@Column(name = "path_", length = 255, unique = true)
	private String path;

	/**
	 * 文件大小
	 */
	@Column(name = "size_")
	private Long size;

	@Override
	public Optional<Audit> getCreatedBy() {
		return Optional.ofNullable(this.createBy);
	}

	@Override
	public Optional<Audit> getLastModifiedBy() {
		return Optional.ofNullable(this.lastModifiedBy);
	}

	@Override
	public void setLastModifiedBy(Audit lastModifiedBy) {
		this.lastModifiedBy = new ModifyAudit(lastModifiedBy);
	}

	@Override
	public void setCreatedBy(Audit createdBy) {
		this.createBy = new CreateAudit(createBy);
	}

	@Override
	public Optional<ZonedDateTime> getCreatedDate() {
		return Optional.ofNullable(this.createdDate);
	}

	@Override
	public void setCreatedDate(ZonedDateTime createDate) {
		this.createdDate = createDate;
	}

	@Override
	public Optional<ZonedDateTime> getLastModifiedDate() {
		return Optional.ofNullable(this.lastModifiedDate);
	}

	@Override
	public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Override
	public UUID getId() {
		return this.id;
	}

	@Override
	public boolean isNew() {
		return Objects.isNull(this.id);
	}

	public String getFilename() {
		return this.filename;
	}

	public String getPath() {
		return this.path;
	}

	public Long getSize() {
		return this.size;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public void setId(UUID id) {
		this.id = id;
	}
}
