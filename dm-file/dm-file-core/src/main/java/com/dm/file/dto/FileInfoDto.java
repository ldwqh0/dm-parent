package com.dm.file.dto;

import com.dm.common.dto.IdentifiableDto;
import com.dm.common.entity.Audit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
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
    private Audit createdBy;

    /**
     * 最后修改人
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Audit lastModifiedBy;

    /**
     * 创建时间
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createTime;

}
