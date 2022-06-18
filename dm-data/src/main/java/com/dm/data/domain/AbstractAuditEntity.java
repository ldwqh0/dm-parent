package com.dm.data.domain;

import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Optional;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AbstractAuditEntity extends AbstractEntity implements Auditable<Auditor<Long, String>, Long, ZonedDateTime> {

    /**
     * 增加几个JSON忽略属性，主要在于不使用DTO的时候，
     */
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "userid", column = @Column(name = "created_user_id_")),
        @AttributeOverride(name = "username", column = @Column(name = "created_user_name_"))
    })
    private SimpleAuditor<Long, String> createdBy;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "userid", column = @Column(name = "last_modified_user_id_")),
        @AttributeOverride(name = "username", column = @Column(name = "last_modified_user_name_"))
    })
    private SimpleAuditor<Long, String> lastModifiedBy;

    /**
     * 创建时间 <br>
     * 字段不能被更新
     */
    @Column(name = "created_time_", updatable = false)
    private ZonedDateTime createdTime;

    @Column(name = "last_modified_time_")
    private ZonedDateTime lastModifiedTime;

    @Override
    public Optional<Auditor<Long, String>> getCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    @Override
    public void setCreatedBy(Auditor<Long, String> createdBy) {
        this.createdBy = new SimpleAuditor<>(createdBy.getUserid(), createdBy.getUsername());
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
    public Optional<Auditor<Long, String>> getLastModifiedBy() {
        return Optional.ofNullable(this.lastModifiedBy);
    }

    @Override
    public void setLastModifiedBy(Auditor<Long, String> lastModifiedBy) {
        this.lastModifiedBy = new SimpleAuditor<>(lastModifiedBy.getUserid(), lastModifiedBy.getUsername());
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
