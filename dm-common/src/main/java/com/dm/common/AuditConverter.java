package com.dm.common;

import com.dm.common.dto.AuditableDto;
import com.dm.common.dto.IdentifiableDto;
import com.dm.common.entity.Audit;
import org.springframework.data.domain.Auditable;

import java.io.Serializable;
import java.time.ZonedDateTime;

public final class AuditConverter {
    private AuditConverter() {
    }

    public static <T extends AuditableDto<UID, UNAME>, UID extends Serializable, UNAME extends Serializable> T toDto(T dist, Auditable<Audit<UID, UNAME>, ?, ZonedDateTime> source) {
        source.getCreatedBy().map(Audit::of).ifPresent(dist::setCreateBy);
        source.getCreatedDate().ifPresent(dist::setCreatedTime);
        source.getLastModifiedBy().map(Audit::of).ifPresent(dist::setLastModifiedBy);
        source.getLastModifiedDate().ifPresent(dist::setLastModifiedTime);
        return dist;
    }

    public static <T extends AuditableDto<UID, UNAME> & IdentifiableDto<ID>, ID extends Serializable, UID extends Serializable, UNAME extends Serializable> T toDtoWithId(T dist, Auditable<Audit<UID, UNAME>, ID, ZonedDateTime> source) {
        dist.setId(source.getId());
        return toDto(dist, source);
    }
}
