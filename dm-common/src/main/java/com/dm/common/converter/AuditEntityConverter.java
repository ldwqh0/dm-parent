package com.dm.common.converter;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.dm.common.dto.AuditableDto;
import com.dm.common.entity.AbstractAuditEntity;

public interface AuditEntityConverter<M extends AbstractAuditEntity, DTO extends AuditableDto>
        extends Converter<M, DTO> {

    public default DTO toDto(M model) {
        if (Objects.isNull(model)) {
            return null;
        } else {
            DTO dto = toDtoWithoutAudit(model);
            return addAuditInfo(dto, model);
        }
    }

    public default DTO addAuditInfo(@NotNull DTO dto, M model) {
        dto.setCreateBy(model.getCreatedBy().orElse(null));
        dto.setLastModifiedBy(model.getLastModifiedBy().orElse(null));
        dto.setCreatedDate(model.getCreatedDate().orElse(null));
        dto.setLastModifiedDate(model.getLastModifiedDate().orElse(null));
        return dto;
    }

    public DTO toDtoWithoutAudit(M model);

}
