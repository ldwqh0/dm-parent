package com.dm.common.dto;

import com.dm.common.entity.Audit;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
public abstract class AuditableDto<UID extends Serializable, UNAME extends Serializable> implements Serializable {
    private static final long serialVersionUID = -5872361624625517456L;

    /**
     * 创建人
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Audit<UID, UNAME> createBy;

    /**
     * 最后修改人
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Audit<UID, UNAME> lastModifiedBy;

    /**
     * 创建时间
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdTime;

    /**
     * 最后修改时间
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime lastModifiedTime;

}
