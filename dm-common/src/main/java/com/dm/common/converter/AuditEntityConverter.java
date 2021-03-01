package com.dm.common.converter;

import com.dm.common.dto.AuditableDto;
import com.dm.common.entity.Audit;
import org.springframework.data.domain.Auditable;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * 包含审计的DTO和模型转换器
 *
 * @param <M>   模型类
 * @param <DTO> DTO类对象
 */
public interface AuditEntityConverter<M extends Auditable<Audit, ? extends Serializable, ZonedDateTime>, DTO extends AuditableDto>
        extends Converter<M, DTO> {

    @Override
    default DTO toDto(@NotNull M model) {
        DTO dto = toDtoWithoutAudit(model);
        return addAuditInfo(dto, model);
    }

    /**
     * 给指定的DTO对象附加审计信息
     *
     * @param dto   dto信息
     * @param model 模型信息
     * @return 包含审计的dto对象
     */
    default DTO addAuditInfo(@NotNull DTO dto, M model) {
        model.getCreatedBy().ifPresent(dto::setCreateBy);
        model.getLastModifiedBy().ifPresent(dto::setLastModifiedBy);
        model.getCreatedDate().ifPresent(dto::setCreatedTime);
        model.getLastModifiedDate().ifPresent(dto::setLastModifiedTime);
        return dto;
    }

    /**
     * 转化为DTO对象，不包含审计信息
     *
     * @param model 数据模型
     * @return 不包含审计信息的Dto对象
     */
    DTO toDtoWithoutAudit(M model);

}
