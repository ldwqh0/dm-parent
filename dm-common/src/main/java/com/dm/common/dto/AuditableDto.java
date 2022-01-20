package com.dm.common.dto;

import com.dm.data.domain.Audit;

import java.io.Serializable;
import java.time.ZonedDateTime;

public interface AuditableDto<UID extends Serializable, UNAME extends Serializable> extends Serializable {

    /**
     * 创建人
     */
    Audit<UID, UNAME> getCreateBy();


    /**
     * 创建人
     */
    Audit<UID, UNAME> getLastModifiedBy();


    /**
     * 创建时间
     */
    ZonedDateTime getCreatedTime();


    /**
     * 最后修改时间
     */
    ZonedDateTime getLastModifiedTime();

}
