package com.dm.file.entity;

import com.dm.data.domain.Auditor;
import com.dm.data.domain.SimpleAuditor;
import com.dm.file.listener.FileListener;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Entity
@EntityListeners({AuditingEntityListener.class, FileListener.class})
@Table(name = "dm_file_", uniqueConstraints = {
    @UniqueConstraint(name = "UK_dm_file_path_", columnNames = {"path_"})
}, indexes = {
    @Index(name = "idx_dm_file_created_date_", columnList = "created_date_"),
    @Index(name = "idx_dm_file_created_user_id_", columnList = "created_user_id_"),
    @Index(name = "idx_dm_file_created_user_name_", columnList = "created_user_name_"),
    @Index(name = "idx_fm_file_sha256_", columnList = "sha256_"),
    @Index(name = "idx_dm_file_md5_", columnList = "md5_"),
    @Index(name = "idx_dm_file_filename_", columnList = "file_name_")
})
public class FileInfo implements Auditable<Auditor<Long, String>, UUID, ZonedDateTime> {

    @Id
    @GeneratedValue(generator = "ordered-uuid")
    @Type(type = "uuid-char")
    @Column(name = "id_", length = 36)
    @GenericGenerator(name = "ordered-uuid", strategy = "org.hibernate.id.UUIDGenerator", parameters = {
        @Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")
    })
    private UUID id;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "userid", column = @Column(name = "created_user_id_")),
        @AttributeOverride(name = "username", column = @Column(name = "created_user_name_"))
    })
    private SimpleAuditor<Long, String> createdBy;

    @Column(name = "created_date_")
    @CreatedDate
    private ZonedDateTime createdDate = ZonedDateTime.now();

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "userid", column = @Column(name = "last_modified_user_id_")),
        @AttributeOverride(name = "username", column = @Column(name = "last_modified_user_name_"))
    })
    private SimpleAuditor<Long, String> lastModifiedBy;

    @Column(name = "last_modified_date_")
    @LastModifiedDate
    private ZonedDateTime lastModifiedDate = ZonedDateTime.now();

    /**
     * 文件名
     */
    @Column(name = "file_name_", nullable = false)
    private String filename;

    /**
     * 文件存储路径
     */
    @Column(name = "path_", length = 1000)
    private String path;

    /**
     * 文件的sha256校验码
     */
    @Column(name = "sha256_", length = 64)
    private String sha256;

    /**
     * 文件的md5校验码
     */
    @Column(name = "md5_", length = 32)
    private String md5;

    /**
     * 文件大小
     */
    @Column(name = "size_")
    private Long size;

    @Override
    public Optional<Auditor<Long, String>> getCreatedBy() {
        return Optional.ofNullable(this.createdBy);
    }

    @Override
    public Optional<Auditor<Long, String>> getLastModifiedBy() {
        return Optional.ofNullable(this.lastModifiedBy);
    }

    @Override
    public void setLastModifiedBy(Auditor<Long, String> lastModifiedBy) {
        this.lastModifiedBy = new SimpleAuditor<>(lastModifiedBy.getUserid(), lastModifiedBy.getUsername());
    }

    @Override
    public void setCreatedBy(Auditor<Long, String> createBy) {
        this.createdBy = new SimpleAuditor<>(createBy.getUserid(), createBy.getUsername());
    }

    @Override
    public Optional<ZonedDateTime> getCreatedDate() {
        return Optional.ofNullable(this.createdDate);
    }

    @Override
    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
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

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
