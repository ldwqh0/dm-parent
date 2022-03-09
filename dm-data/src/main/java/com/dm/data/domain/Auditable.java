package com.dm.data.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Optional;

public interface Auditable<UID extends Serializable, UNAME extends Serializable> extends Serializable {

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
