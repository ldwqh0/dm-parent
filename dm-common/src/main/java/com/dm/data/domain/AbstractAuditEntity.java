package com.dm.data.domain;

import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.ZonedDateTime;
import java.util.Optional;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AbstractAuditEntity extends AbstractEntity implements Auditable<Audit<Long, String>, Long, ZonedDateTime> {

    /**
     * 增加几个JSON忽略属性，主要在于不使用DTO的时候，
     */
    @Embedded
    private CreateAudit createdBy;

    @Embedded
    private ModifyAudit lastModifiedBy;

    /**
     * 创建时间 <br>
     * 字段不能被更新
     */
    @Column(name = "created_time_", updatable = false)
    private ZonedDateTime createdTime;

    @Column(name = "last_modified_time_")
    private ZonedDateTime lastModifiedTime;

    @Override
    public Optional<Audit<Long, String>> getCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    @Override
    public void setCreatedBy(Audit<Long, String> createdBy) {
        this.createdBy = new CreateAudit(createdBy);
    }

    @Override
    public Optional<ZonedDateTime> getCreatedDate() {
        return Optional.ofNullable(this.createdTime);
    }

    @Override
    public void setCreatedDate(ZonedDateTime creationDate) {
        this.createdTime = creationDate;
    }

    @Override
    public Optional<Audit<Long, String>> getLastModifiedBy() {
        return Optional.ofNullable(this.lastModifiedBy);
    }

    @Override
    public void setLastModifiedBy(Audit<Long, String> lastModifiedBy) {
        this.lastModifiedBy = new ModifyAudit(lastModifiedBy);
    }

    @Override
    public Optional<ZonedDateTime> getLastModifiedDate() {
        return Optional.ofNullable(this.lastModifiedTime);
    }

    @Override
    public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedTime = lastModifiedDate;
    }
}
