package com.dm.common.converter;

import javax.annotation.Nullable;

/**
 * 将一个Model对象转换为另外一个DTO对象的方法
 *
 * @param <M>   来源Model对象
 * @param <DTO> 目标DTO对象
 */
public interface Converter<M, DTO> {

    /**
     * 将实体安静的转换为Dto对象，<br>
     *
     * @return 转换后的dto对象
     * @since 0.2.2
     */
    @Nullable
    DTO toDto(@Nullable M model);

    /**
     * 将DTO中的数据复制到实体模型<br />
     * <p>
     * 所有的Converter必须实现该方法<br />
     * 注意在复制数据的时候，如果存在关联关系，复制不应该在converter进行<br />
     * converter不应该实现任何关联Repository的操做，它仅仅只作为一个复制数据的工具出现的
     *
     * @param model 模型
     * @param dto   dto
     */
    M copyProperties(M model, DTO dto);

}
