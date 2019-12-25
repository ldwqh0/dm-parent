package com.dm.common.converter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.Nullable;

import org.apache.commons.collections4.IterableUtils;

public interface Converter<M, DTO> {

    /**
     * 将实体安静的转换为Dto对象，<br>
     * 
     * 
     * @since 0.2.2
     * 
     * @return 转换后的dto对象
     */
    @Nullable
    public DTO toDto(@Nullable M model);

    /**
     * 将实体模型列表转换为DTO列表
     * 
     * @param models
     * @return
     */
    public default List<DTO> toDto(Iterable<M> models) {
        if (IterableUtils.isEmpty(models)) {
            return Collections.emptyList();
        } else {
            return StreamSupport.stream(models.spliterator(), false).map(this::toDto)
                    .collect(Collectors.toList());
        }
    }

    /**
     * 将DTO中的数据复制到实体模型<br />
     * 
     * 所有的Converter必须实现该方法<br />
     * 注意在复制数据的时候，如果存在关联关系，复制不应该在converter进行<br />
     * converter不应该实现任何关联Respository的操做，它仅仅只作为一个复制数据的工具出现的
     * 
     * @param model
     * @param dto
     */
    public abstract M copyProperties(M model, DTO dto);

}