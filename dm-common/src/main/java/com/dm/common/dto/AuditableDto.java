package com.dm.common.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;

import com.dm.common.entity.Audit;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;
import lombok.Setter;

@Data
public class AuditableDto implements Serializable {
    private static final long serialVersionUID = -5872361624625517456L;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Audit createBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Audit lastModifiedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime lastModifiedDate;

}
