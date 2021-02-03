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
import lombok.Setter;

@Data
@JsonInclude(value = Include.NON_EMPTY)
public class FileInfoDto implements IdentifiableDto<UUID>, Serializable {

    private static final long serialVersionUID = -6472426570089325611L;

    private UUID id;

    private String filename;

    @JsonIgnore
    private String path;

    private Long size;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Audit createUser;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Audit lastModifiedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createTime;

}
