package com.dm.file.dto;

import com.dm.common.dto.IdentifiableDto;
import com.dm.common.entity.Audit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@JsonInclude(value = Include.NON_EMPTY)
public class FileInfoDto implements IdentifiableDto<UUID>, Serializable {

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
    private final Long size;

    /**
     * 创建人
     */
    @JsonProperty(access = READ_ONLY)
    private final Audit<Long, String> createdBy;

    /**
     * 最后修改人
     */
    @JsonProperty(access = READ_ONLY)
    private final Audit<Long, String> lastModifiedBy;

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

    public Audit<Long, String> getCreatedBy() {
        return createdBy;
    }

    public Audit<Long, String> getLastModifiedBy() {
        return lastModifiedBy;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public FileInfoDto(UUID id, String filename, String path, Long size, Audit<Long, String> createdBy, Audit<Long, String> lastModifiedBy, ZonedDateTime createTime) {
        this.id = id;
        this.filename = filename;
        this.path = path;
        this.size = size;
        this.createdBy = createdBy;
        this.lastModifiedBy = lastModifiedBy;
        this.createTime = createTime;
    }

    public FileInfoDto(String filename, Long size) {
        this(
            null,
            filename,
            null,
            size,
            null,
            null,
            null
        );
    }
}
