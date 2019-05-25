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
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "dm_file_")
@EntityListeners(AuditingEntityListener.class)
public class FileInfo implements Auditable<String, UUID, ZonedDateTime>, Serializable {
	private static final long serialVersionUID = -914974010332311193L;

	@Id
	@GeneratedValue
	@Type(type = "uuid-char")
	@Column(name = "id_", length = 36)
	private @Nullable UUID id;

	@CreatedBy
	@Column(name = "create_by_", length = 50)
	private String createBy;

	@Column(name = "created_date_")
	@CreatedDate
	private ZonedDateTime createdDate;

	@LastModifiedBy
	@Column(name = "last_modified_by", length = 50)
	private String lastModifiedBy;

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
	public Optional<String> getCreatedBy() {
		return Optional.ofNullable(this.createBy);
	}

	@Override
	public Optional<String> getLastModifiedBy() {
		return Optional.ofNullable(this.lastModifiedBy);
	}

	@Override
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Override
	public void setCreatedBy(String createdBy) {
		this.createBy = createdBy;
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
