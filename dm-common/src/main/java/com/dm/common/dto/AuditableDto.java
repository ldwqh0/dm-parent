package com.dm.common.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;

import com.dm.common.entity.Audit;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class AuditableDto implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -5872361624625517456L;

    @JsonProperty(access = Access.READ_ONLY)
    private Audit createBy;

    @JsonProperty(access = Access.READ_ONLY)
    private Audit lastModifiedBy;

    @JsonProperty(access = Access.READ_ONLY)
    private ZonedDateTime createdDate;

    @JsonProperty(access = Access.READ_ONLY)
    private ZonedDateTime lastModifiedDate;

    public Audit getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Audit createBy) {
        this.createBy = createBy;
    }

    public Audit getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Audit lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

}
