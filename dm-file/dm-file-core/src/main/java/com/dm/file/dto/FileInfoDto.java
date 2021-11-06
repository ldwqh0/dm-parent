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

@JsonInclude(value = Include.NON_EMPTY)
public class FileInfoDto implements IdentifiableDto<UUID>, Serializable {

    private static final long serialVersionUID = -6472426570089325611L;

    /**
     * 文件ID
     */
    private UUID id;

    /**
     * 文件原始名称
     */
    private String filename;

    /**
     * 文件保存路径
     */
    @JsonIgnore
    private String path;

    /**
     * 文件长度
     */
    private Long size;

    /**
     * 创建人
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Audit<Long, String> createdBy;

    /**
     * 最后修改人
     */
    //TODO findbugs故障
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Audit<Long, String> lastModifiedBy;

    /**
     * 创建时间
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createTime;


    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Audit<Long, String> getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Audit<Long, String> createdBy) {
        this.createdBy = createdBy;
    }

    public Audit<Long, String> getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Audit<Long, String> lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }
}
