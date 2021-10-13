package com.dm.common.dto;

import com.dm.common.entity.Audit;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

public abstract class AuditableDto<UID extends Serializable, UNAME extends Serializable> implements Serializable {
    private static final long serialVersionUID = -5872361624625517456L;

    /**
     * 创建人
     *
     * @ignore
     */
    private Audit<UID, UNAME> createBy;

    /**
     * 最后修改人
     *
     * @ignore
     */
    private Audit<UID, UNAME> lastModifiedBy;

    /**
     * 创建时间
     *
     * @ignore
     */
    private ZonedDateTime createdTime;

    /**
     * 最后修改时间
     *
     * @ignore
     */
    private ZonedDateTime lastModifiedTime;

    /**
     * 创建人
     */
    public Audit<UID, UNAME> getCreateBy() {
        return createBy;
    }

    /**
     * 创建人
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public void setCreateBy(Audit<UID, UNAME> createBy) {
        this.createBy = createBy;
    }

    /**
     * 创建人
     */
    public Audit<UID, UNAME> getLastModifiedBy() {
        return lastModifiedBy;
    }

    /**
     * 最后修改人
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public void setLastModifiedBy(Audit<UID, UNAME> lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    /**
     * 创建时间
     */
    public ZonedDateTime getCreatedTime() {
        return createdTime;
    }

    /**
     * 创建时间
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * 最后修改时间
     */
    public ZonedDateTime getLastModifiedTime() {
        return lastModifiedTime;
    }

    /**
     * 最后修改时间
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public void setLastModifiedTime(ZonedDateTime lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditableDto<?, ?> that = (AuditableDto<?, ?>) o;
        return Objects.equals(createBy, that.createBy) && Objects.equals(lastModifiedBy, that.lastModifiedBy) && Objects.equals(createdTime, that.createdTime) && Objects.equals(lastModifiedTime, that.lastModifiedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createBy, lastModifiedBy, createdTime, lastModifiedTime);
    }
}
