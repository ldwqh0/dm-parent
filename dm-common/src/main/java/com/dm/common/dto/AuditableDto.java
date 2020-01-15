package com.dm.common.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;

import com.dm.common.entity.Audit;

import lombok.Data;

@Data
public class AuditableDto implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -5872361624625517456L;

    private Audit createBy;

    private Audit lastModifiedBy;

    private ZonedDateTime createdDate;

    private ZonedDateTime lastModifiedDate;
}
