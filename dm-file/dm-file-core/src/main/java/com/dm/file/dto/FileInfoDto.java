package com.dm.file.dto;

import com.dm.data.domain.Auditor;
import com.dm.data.domain.Identifiable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@JsonInclude(NON_EMPTY)
public class FileInfoDto implements Identifiable<UUID>, Serializable {

    private static final long serialVersionUID = -6472426570089325611L;

    /**
     * 文件ID
     */
    private final UUID id;

    /**
     * 文件原始名称
     */
    private final String filename;

    /**
     * 文件保存路径
     */
    @JsonIgnore
    private final String path;

    /**
     * 文件长度
     */
    @JsonProperty(access = READ_ONLY)
    private final Long size;

    /**
     * 创建人
     */
    @JsonProperty(access = READ_ONLY)
    private final Auditor<Long, String> createdBy;

    /**
     * 最后修改人
     */
    @JsonProperty(access = READ_ONLY)
    private final Auditor<Long, String> lastModifiedBy;

    /**
     * 创建时间
     */
    @JsonProperty(access = READ_ONLY)
    private final ZonedDateTime createTime;

    @Override
    public UUID getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public String getPath() {
        return path;
    }

    public Long getSize() {
        return size;
    }

    public Optional<Auditor<Long, String>> getCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public Auditor<Long, String> getLastModifiedBy() {
        return lastModifiedBy;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    private FileInfoDto() {
        this(null, null, null, null, null, null, null);
    }

    private FileInfoDto(UUID id, String filename, String path, Long size, Auditor<Long, String> createdBy, Auditor<Long, String> lastModifiedBy, ZonedDateTime createTime) {
        this.id = id;
        this.filename = filename;
        this.path = path;
        this.size = size;
        this.createdBy = createdBy;
        this.lastModifiedBy = lastModifiedBy;
        this.createTime = createTime;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private UUID id;
        private String filename;
        private String path;
        private Long size;
        private Auditor<Long, String> createdBy;
        private Auditor<Long, String> lastModifiedBy;
        private ZonedDateTime createTime;

        private Builder() {
        }

        public static Builder aFileInfoDto() {
            return new Builder();
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder filename(String filename) {
            this.filename = filename;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder size(Long size) {
            this.size = size;
            return this;
        }

        public Builder createdBy(Auditor<Long, String> createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder lastModifiedBy(Auditor<Long, String> lastModifiedBy) {
            this.lastModifiedBy = lastModifiedBy;
            return this;
        }

        public Builder createTime(ZonedDateTime createTime) {
            this.createTime = createTime;
            return this;
        }

        public FileInfoDto build() {
            return new FileInfoDto(id, filename, path, size, createdBy, lastModifiedBy, createTime);
        }
    }
}
