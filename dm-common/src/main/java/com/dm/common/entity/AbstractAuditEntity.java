package com.dm.common.entity;

import java.time.ZonedDateTime;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditEntity extends AbstractEntity implements Auditable<Audit, Long, ZonedDateTime> {

  private static final long serialVersionUID = -3422581450045291219L;

  /**
   * 增加几个JSON忽略属性，主要在于不使用DTO的时候，
   */
  @JsonProperty(access = Access.READ_ONLY)
  private CreateAudit createdBy;

  @JsonProperty(access = Access.READ_ONLY)
  private ModifyAudit lastModifiedBy;

  /**
   * 创建时间字段不能被更新
   */
  @Column(name = "created_date_", updatable = false)
  @JsonProperty(access = Access.READ_ONLY)
  private ZonedDateTime createdDate;

  @Column(name = "last_modified_date_")
  @JsonProperty(access = Access.READ_ONLY)
  private ZonedDateTime lastModifiedDate;

  @Override
  public Optional<Audit> getCreatedBy() {
    return Optional.ofNullable(this.createdBy);
  }

  @Override
  public void setCreatedBy(Audit createdBy) {
    this.createdBy = new CreateAudit(createdBy);
  }

  @Override
  public Optional<ZonedDateTime> getCreatedDate() {
    return Optional.ofNullable(this.createdDate);
  }

  @Override
  public void setCreatedDate(ZonedDateTime createdDate) {
    this.createdDate = createdDate;
  }

  @Override
  public Optional<Audit> getLastModifiedBy() {
    return Optional.ofNullable(this.lastModifiedBy);
  }

  @Override
  public void setLastModifiedBy(Audit lastModifiedBy) {
    this.lastModifiedBy = new ModifyAudit(lastModifiedBy);
  }

  @Override
  public Optional<ZonedDateTime> getLastModifiedDate() {
    return Optional.ofNullable(this.lastModifiedDate);
  }

  @Override
  public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }
}
