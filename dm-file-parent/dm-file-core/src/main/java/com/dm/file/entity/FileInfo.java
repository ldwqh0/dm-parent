package com.dm.file.entity;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dm.common.entity.Audit;
import com.dm.common.entity.CreateAudit;
import com.dm.common.entity.ModifyAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "dm_file_", indexes = {
        @Index(name = "idx_dm_file_created_date_", columnList = "created_date_"),
        @Index(name = "idx_dm_file_created_user_id_", columnList = "created_user_id_"),
        @Index(name = "idx_dm_file_created_user_name_", columnList = "created_user_name_")
})
@JsonIgnoreProperties(value = { "lastModifiedBy", "createdBy", "createdDate", "lastModifiedDate" }, allowGetters = true)
public class FileInfo implements Auditable<Audit, UUID, ZonedDateTime>, Serializable {
    private static final long serialVersionUID = -914974010332311193L;

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    @Column(name = "id_", length = 36)
    private UUID id;

    private CreateAudit createdBy;

    @Column(name = "created_date_")
    @CreatedDate
    private ZonedDateTime createdDate = ZonedDateTime.now();

    private ModifyAudit lastModifiedBy;

    @Column(name = "last_modified_date_")
    @LastModifiedDate
    private ZonedDateTime lastModifiedDate = ZonedDateTime.now();

    /**
     * 文件名
     */
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
        return Optional.ofNullable(this.createdBy);
    }

    @Override
    public Optional<Audit> getLastModifiedBy() {
        return Optional.ofNullable(this.lastModifiedBy);
    }

    @Override
    @JsonProperty(access = Access.READ_ONLY)
    public void setLastModifiedBy(Audit lastModifiedBy) {
        this.lastModifiedBy = new ModifyAudit(lastModifiedBy);
    }

    @Override
    @JsonProperty(access = Access.READ_ONLY)
    public void setCreatedBy(Audit createBy) {
        this.createdBy = new CreateAudit(createBy);
    }

    @Override
    public Optional<ZonedDateTime> getCreatedDate() {
        return Optional.ofNullable(this.createdDate);
    }

    @Override
    @JsonProperty(access = Access.READ_ONLY)
    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public Optional<ZonedDateTime> getLastModifiedDate() {
        return Optional.ofNullable(this.lastModifiedDate);
    }

    @Override
    @JsonProperty(access = Access.READ_ONLY)
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
