package com.dm.common.dto;

import com.dm.data.domain.Audit;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Optional;

public interface AuditableDto<UID extends Serializable, UNAME extends Serializable> extends Serializable {

    /**
     * 创建人
     */
    Optional<Audit<UID, UNAME>> getCreatedBy();

    /**
     * 创建人
     */
    Optional<Audit<UID, UNAME>> getLastModifiedBy();

    /**
     * 创建时间
     */
    Optional<ZonedDateTime> getCreatedTime();

    /**
     * 最后修改时间
     */
    Optional<ZonedDateTime> getLastModifiedTime();

}
