package com.dm.common.dto;

import com.dm.common.entity.Audit;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
public class AuditableDto implements Serializable {
    private static final long serialVersionUID = -5872361624625517456L;

    /**
     * 创建人
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Audit<Long,String> createBy;

    /**
     * 最后修改人
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Audit<Long,String> lastModifiedBy;

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
