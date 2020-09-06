package com.dm.file.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

import com.dm.common.dto.IdentifiableDto;
import com.dm.common.entity.Audit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_EMPTY)
public class FileInfoDto implements IdentifiableDto<UUID>, Serializable {

    private static final long serialVersionUID = -6472426570089325611L;

    private UUID id;

    private String filename;

    @JsonIgnore
    private String path;

    private Long size;

    @JsonProperty(access = Access.READ_ONLY)
    private Audit createUser;

    @JsonProperty(access = Access.READ_ONLY)
    private Audit lastModifiedBy;

    @JsonProperty(access = Access.READ_ONLY)
    private ZonedDateTime createTime;

    public Audit getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Audit createUser) {
        this.createUser = createUser;
    }

    public Audit getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Audit lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

}
